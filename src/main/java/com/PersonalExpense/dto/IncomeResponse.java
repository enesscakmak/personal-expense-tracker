package com.PersonalExpense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeResponse (
    Long id,
    String name,
    BigDecimal amount,
    String description,
    LocalDate date,
    String source
) {}
