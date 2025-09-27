package com.example.bank_midterm.tuition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface TuitionRepository extends JpaRepository<Tuition, UUID> {
    List<Tuition> findByStudentCode(String studentCode);
}
