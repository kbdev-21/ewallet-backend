package com.example.bank_midterm.service.impl;

import com.example.bank_midterm.dto.transaction.ConfirmTransactionRequest;
import com.example.bank_midterm.dto.transaction.InitTransactionRequest;
import com.example.bank_midterm.dto.transaction.TransactionResponse;
import com.example.bank_midterm.entity.Otp;
import com.example.bank_midterm.entity.Transaction;
import com.example.bank_midterm.entity.Tuition;
import com.example.bank_midterm.entity.User;
import com.example.bank_midterm.exception.CustomException;
import com.example.bank_midterm.repository.TransactionRepository;
import com.example.bank_midterm.repository.TuitionRepository;
import com.example.bank_midterm.repository.TuitionTransactionRepository;
import com.example.bank_midterm.repository.UserRepository;
import com.example.bank_midterm.security.AuthUtil;
import com.example.bank_midterm.service.NotificationService;
import com.example.bank_midterm.service.OtpService;
import com.example.bank_midterm.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final NotificationService notificationService;
    private final TransactionRepository transactionRepository;
    private final AuthUtil authUtil;
    private final OtpService otpService;
    private final UserRepository userRepository;
    private final TuitionRepository tuitionRepository;
    private final TuitionTransactionRepository tuitionTransactionRepository;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(
        NotificationService notificationService,
        TransactionRepository transactionRepository,
        AuthUtil authUtil,
        OtpService otpService,
        UserRepository userRepository,
        TuitionRepository tuitionRepository,
        TuitionTransactionRepository tuitionTransactionRepository,
        ModelMapper modelMapper
    ) {
        this.notificationService = notificationService;
        this.transactionRepository = transactionRepository;
        this.authUtil = authUtil;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.tuitionRepository = tuitionRepository;
        this.tuitionTransactionRepository = tuitionTransactionRepository;
        this.modelMapper = modelMapper;
    }

    public TransactionResponse getCompletedTransactionById(UUID transactionId) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Transaction not found")
        );

        if (transaction.getStatus() != Transaction.Status.COMPLETED) {
            throw new CustomException(HttpStatus.CONFLICT, "Transaction is not completed");
        }

        return modelMapper.map(transaction, TransactionResponse.class);
    }

    public List<TransactionResponse> getCompletedTransactionsByUserId(UUID userId) {
        List<Transaction> transactions = new ArrayList<>();

        transactions.addAll(transactionRepository.findByReceiverId(userId));
        transactions.addAll(transactionRepository.findBySenderId(userId));

        return transactions
            .stream()
            .filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED)
            .sorted(Comparator.comparing(Transaction::getCompletedAt).reversed())
            .map(t -> modelMapper.map(t, TransactionResponse.class))
            .toList();
    }

    @Transactional
    public TransactionResponse initTransaction(InitTransactionRequest request) {
        User requester = authUtil.getAuthenticatedUser();
        if(!requester.getId().equals(request.getSenderId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "User not allowed");
        }

        User sender = userRepository.findById(request.getSenderId()).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Sender not found")
        );
        User receiver = userRepository.findById(request.getReceiverId()).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Receiver not found")
        );

        validateSenderAndReceiver(sender, receiver, request.getAmount());

        Otp newOtp = otpService.createOtp(5 * 60 * 1000);
        notificationService.sendEmail(sender.getEmail(),
            "Confirm OTP code",
            "Use this code to confirm your transaction: " + newOtp.getCode()
        );

        Transaction newTransaction = new Transaction();
        newTransaction.setId(UUID.randomUUID());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setAmount(request.getAmount());
        newTransaction.setFee(calculateFee(request.getAmount()));
        newTransaction.setMessage(request.getMessage() != null ? request.getMessage() : "");
        newTransaction.setOtpId(newOtp.getId());
        newTransaction.setType(request.getType() == null ? Transaction.Type.TRANSFER : request.getType());
        newTransaction.setStatus(Transaction.Status.PENDING);
        newTransaction.setInitAt(new Date());
        newTransaction.setCompletedAt(null);

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    @Transactional
    public TransactionResponse confirmTransaction(ConfirmTransactionRequest request) {
        Transaction confirmingTransaction = transactionRepository.findById(request.getTransactionId()).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Transaction not found")
        );

        if (confirmingTransaction.getStatus() != Transaction.Status.PENDING) {
            throw new CustomException(HttpStatus.CONFLICT, "Transaction already completed or cancelled");
        }

        User requester = authUtil.getAuthenticatedUser();
        User sender = confirmingTransaction.getSender();
        User receiver = confirmingTransaction.getReceiver();

        if (!requester.getId().equals(sender.getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "User not allowed");
        }

        validateSenderAndReceiver(sender, receiver, confirmingTransaction.getAmount());

        otpService.validateOtp(confirmingTransaction.getOtpId(), request.getConfirmOtpCode());

        additionalProcess(confirmingTransaction);

        BigDecimal senderBill = confirmingTransaction.getAmount().add(confirmingTransaction.getFee());
        sender.setBalance(sender.getBalance().subtract(senderBill));
        receiver.setBalance(receiver.getBalance().add(confirmingTransaction.getAmount()));

        confirmingTransaction.setStatus(Transaction.Status.COMPLETED);
        confirmingTransaction.setCompletedAt(new Date());

        userRepository.saveAll(List.of(sender, receiver));
        Transaction savedTransaction = transactionRepository.save(confirmingTransaction);

        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    private void validateSenderAndReceiver(User sender, User receiver, BigDecimal amount) {
        if (sender.getId().equals(receiver.getId())) {
            throw new CustomException(HttpStatus.CONFLICT, "Sender and receiver are the same person");
        }

        var paidAmount = amount.add(calculateFee(amount));
        if (sender.getBalance().compareTo(paidAmount) < 0) {
            throw new CustomException(HttpStatus.CONFLICT, "Sender does not have enough money");
        }
    }

    /* TODO:  */
    private BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal RATE = new BigDecimal("0.001");
        return amount.multiply(RATE);
    }

    private void additionalProcess(Transaction confirmingTransaction) {
        if(confirmingTransaction.getType() == Transaction.Type.TUITION_PAYMENT) {
            var tuitionTransactionRow = tuitionTransactionRepository.findById(confirmingTransaction.getId()).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "Transaction row not found")
            );
            var tuition = tuitionRepository.findById(tuitionTransactionRow.getTuitionId()).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "Tuition not found")
            );

            if(tuition.getStatus() == Tuition.Status.PAID) {
                throw new CustomException(HttpStatus.CONFLICT, "Tuition is paid");
            }

            var now = new Date();
            if(tuition.getExpiredAt().compareTo(now) < 0) {
                throw new CustomException(HttpStatus.CONFLICT, "Tuition is expired");
            }

            tuition.setStatus(Tuition.Status.PAID);
            tuition.setPaidAt(new Date());
            tuitionRepository.save(tuition);
        }
    }
}
