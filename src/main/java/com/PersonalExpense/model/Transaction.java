package com.PersonalExpense.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private Long userId;
    private String name;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public Transaction(){}

    public Transaction(Long id, Long userId, String name, BigDecimal amount, String description, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public abstract BigDecimal getSignedAmount();
}
