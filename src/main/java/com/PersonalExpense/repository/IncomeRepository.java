package com.PersonalExpense.repository;

import com.PersonalExpense.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends TransactionRepository<Income> {

    @Query("""
    SELECT i FROM Income i WHERE i.userId = :userId
    AND (:startDate IS NULL OR i.date >= :startDate)
    AND (:endDate IS NULL OR i.date <= :endDate)
    AND (:source IS NULL OR i.source = :source)
    """)
    List<Income> findByUserIdWithFilters(Long userId, LocalDate startDate, LocalDate endDate, String source);

    @Query("""
    SELECT i FROM Income i WHERE i.userId = :userId
    AND (:startDate IS NULL OR i.date >= :startDate)
    AND (:endDate IS NULL OR i.date <= :endDate)
    AND (:source IS NULL OR i.source = :source)
    """)
    Page<Income> findByUserIdWithFilters(Long userId, LocalDate startDate, LocalDate endDate, String source, Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(i.amount), 0)
    FROM Income i
    WHERE i.userId = :userId
      AND i.source = :source
    """)
    BigDecimal getTotalAmountByUserIdAndSource(Long userId, String source);
}
