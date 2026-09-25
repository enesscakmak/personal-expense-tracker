package com.PersonalExpense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {
    @NotBlank(message  = "Transaction name cannot be null!")
    private String name;
    @NotNull(message = "Transaction needs an amount!")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;
    private String description;
    @NotNull(message = "Transaction date should be given.")
    private LocalDate date;

    public TransactionRequest(){}

    public TransactionRequest(String name, BigDecimal amount, String description, LocalDate date) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
