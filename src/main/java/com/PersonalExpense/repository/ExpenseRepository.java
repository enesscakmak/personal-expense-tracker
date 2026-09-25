package com.PersonalExpense.repository;


import com.PersonalExpense.model.Category;
import com.PersonalExpense.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends TransactionRepository<Expense>{

    @Query("""
    SELECT e FROM Expense e WHERE e.userId = :userId
    AND (:startDate IS NULL OR e.date >= :startDate)
    AND (:endDate IS NULL OR e.date <= :endDate)
    AND (:category IS NULL OR e.category = :category)
    """)
    List<Expense> findByUserIdWithFilters(Long userId, LocalDate startDate, LocalDate endDate, Category category);

    @Query("""
    SELECT e FROM Expense e WHERE e.userId = :userId
    AND (:startDate IS NULL OR e.date >= :startDate)
    AND (:endDate IS NULL OR e.date <= :endDate)
    AND (:category IS NULL OR e.category = :category)
    """)
    Page<Expense> findByUserIdWithFilters(Long userId, LocalDate startDate, LocalDate endDate, Category category, Pageable pageable);

    @Query("""
    SELECT e
    FROM Expense e
    WHERE e.userId = :userId
      AND e.category = :category
""")
    List<Expense> findByUserIdAndCategory(
            Long userId,
            Category category
    );

    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.userId = :userId
      AND e.category = :category
""")
    BigDecimal getTotalAmountByUserIdAndCategory(
            Long userId,
            Category category
    );
}
