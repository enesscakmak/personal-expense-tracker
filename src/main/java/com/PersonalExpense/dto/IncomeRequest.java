package com.PersonalExpense.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeRequest extends TransactionRequest{
    private String source;

    public IncomeRequest(){}

    public IncomeRequest(String name, BigDecimal amount, String description, String source, LocalDate date) {
       super(name, amount, description, date);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
