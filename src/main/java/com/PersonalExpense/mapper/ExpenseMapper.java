package com.PersonalExpense.mapper;
import com.PersonalExpense.dto.ExpenseRequest;
import com.PersonalExpense.dto.ExpenseResponse;
import com.PersonalExpense.model.Expense;


public class ExpenseMapper {
    public static Expense toEntity(ExpenseRequest request) {
        Expense expense = new Expense();

        expense.setName(request.getName());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense){
        return new ExpenseResponse(
                expense.getId(),
                expense.getName(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getDate(),
                expense.getCategory()
        );
    }

}
