package com.PersonalExpense.model;


import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense extends Transaction{


    private Category category;

    public Expense(){}

    @Override
    public BigDecimal getSignedAmount() {
        return getAmount();
    }

    public Expense(Long id, Long userId, String name, BigDecimal amount, String description, LocalDate date, Category category) {
        super(id, userId, name, amount, description, date);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
