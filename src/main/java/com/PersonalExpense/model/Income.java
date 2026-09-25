package com.PersonalExpense.model;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Income extends Transaction{
    private String source;

    public Income(){}

    @Override
    public BigDecimal getSignedAmount() {
        return getAmount();
    }

    public Income(Long id, Long userId, String name, BigDecimal amount, String description, LocalDate date, String source) {
        super(id, userId, name, amount, description, date);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
