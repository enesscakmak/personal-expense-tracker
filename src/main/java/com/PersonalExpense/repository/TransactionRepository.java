package com.PersonalExpense.repository;

import com.PersonalExpense.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDate;
import java.util.List;

@NoRepositoryBean
public interface TransactionRepository<T extends Transaction> extends JpaRepository<T, Long> {
    List<T> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Query(value = "SELECT t FROM #{#entityName} t WHERE t.userId = :userId " + "AND (:startDate IS NULL OR t.date >= :startDate) " + "AND (:endDate IS NULL OR t.date <= :endDate)")
    Page<T> findByUserIdWithinDateRange(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

