package com.PersonalExpense.dto;

import com.PersonalExpense.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse (
        Long id,
        String name,
        BigDecimal amount,
        String description,
        LocalDate date,
        Category category
){}