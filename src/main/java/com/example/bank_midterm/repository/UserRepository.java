package com.example.bank_midterm.repository;

import com.example.bank_midterm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNum(String phoneNum);
    Optional<User> findByEmail(String email);
}
