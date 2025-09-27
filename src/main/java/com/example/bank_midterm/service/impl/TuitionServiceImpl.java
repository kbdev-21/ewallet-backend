package com.example.bank_midterm.service.impl;

import com.example.bank_midterm.dto.transaction.InitTransactionRequest;
import com.example.bank_midterm.dto.transaction.TransactionResponse;
import com.example.bank_midterm.dto.tuition.CreateTuitionRequest;
import com.example.bank_midterm.dto.tuition.TuitionPaymentRequest;
import com.example.bank_midterm.dto.tuition.TuitionResponse;
import com.example.bank_midterm.entity.Transaction;
import com.example.bank_midterm.entity.Tuition;
import com.example.bank_midterm.entity.TuitionTransaction;
import com.example.bank_midterm.exception.CustomException;
import com.example.bank_midterm.repository.TuitionRepository;
import com.example.bank_midterm.repository.TuitionTransactionRepository;
import com.example.bank_midterm.repository.UserRepository;
import com.example.bank_midterm.security.AuthUtil;
import com.example.bank_midterm.service.TransactionService;
import com.example.bank_midterm.service.TuitionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TuitionServiceImpl implements TuitionService {
    private final TransactionService transactionService;
    private final TuitionRepository tuitionRepository;
    private final UserRepository userRepository;
    private final TuitionTransactionRepository tuitionTransactionRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    public TuitionServiceImpl(
        TransactionService transactionService,
        TuitionRepository tuitionRepository,
        UserRepository userRepository,
        TuitionTransactionRepository tuitionTransactionRepository,
        AuthUtil authUtil,
        ModelMapper modelMapper
    ) {
        this.transactionService = transactionService;
        this.tuitionRepository = tuitionRepository;
        this.userRepository = userRepository;
        this.tuitionTransactionRepository = tuitionTransactionRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public Tuition createTuition(CreateTuitionRequest request) {
        var receiver = userRepository.findById(request.getReceiverId()).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Receiver not found")
        );

        var newTuition = new Tuition();
        newTuition.setId(UUID.randomUUID());
        newTuition.setReceiver(receiver);
        newTuition.setStudentCode(request.getStudentCode());
        newTuition.setSchoolName(request.getSchoolName());
        newTuition.setTitle(request.getTitle());
        newTuition.setAmount(request.getAmount());
        newTuition.setCreatedAt(new Date());
        newTuition.setExpiredAt(request.getExpiredAt());
        newTuition.setStatus(Tuition.Status.UNPAID);

        return tuitionRepository.save(newTuition);
    }

    @Override
    public TuitionResponse getTuitionById(UUID id) {
        return modelMapper.map(tuitionRepository.findById(id), TuitionResponse.class);
    }

    @Override
    public List<TuitionResponse> getTuitionByStudentCode(String studentCode) {
        return tuitionRepository
            .findByStudentCode(studentCode)
            .stream()
            .sorted(
                Comparator
                    .comparing((Tuition t) -> t.getStatus() == Tuition.Status.UNPAID ? 0 : 1)
                    .thenComparing(Comparator.comparing(Tuition::getCreatedAt).reversed())
            )
            .map(t -> modelMapper.map(t, TuitionResponse.class))
            .toList();
    }

    @Override
    @Transactional
    public TransactionResponse initTuitionPayment(TuitionPaymentRequest request) {
        var requester = authUtil.getAuthenticatedUser();
        if(!requester.getId().equals(request.getPayerId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "User not allowed");
        }

        var tuition = tuitionRepository.findById(request.getTuitionId()).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "Tuition not found")
        );

        if(request.getPayerId().equals(tuition.getReceiver().getId())) {
            throw new CustomException(HttpStatus.CONFLICT, "User not allowed");
        }

        if(tuition.getStatus() == Tuition.Status.PAID) {
            throw new CustomException(HttpStatus.CONFLICT, "Tuition is paid");
        }

        var now = new Date();
        if(tuition.getExpiredAt().compareTo(now) < 0) {
            throw new CustomException(HttpStatus.CONFLICT, "Tuition is expired");
        }

        String message = tuition.getSchoolName() + "-" + tuition.getStudentCode() + "-" + tuition.getTitle();
        var initTransactionRequest = new InitTransactionRequest(
            request.getPayerId(),
            tuition.getReceiver().getId().toString(),
            tuition.getAmount(),
            message,
            Transaction.Type.TUITION_PAYMENT
        );
        var initialedTransaction = transactionService.initTransaction(initTransactionRequest);
        tuitionTransactionRepository.save(new TuitionTransaction(
            initialedTransaction.getId(),
            tuition.getId()
        ));

        return initialedTransaction;
    }
}
