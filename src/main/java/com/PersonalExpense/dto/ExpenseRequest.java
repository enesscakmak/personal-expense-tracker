package com.PersonalExpense.dto;

import com.PersonalExpense.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest extends TransactionRequest {
    private Category category;

    public ExpenseRequest(){}

    public ExpenseRequest(String name, BigDecimal amount, String description, Category category, LocalDate date) {
        super(name, amount, description, date);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
