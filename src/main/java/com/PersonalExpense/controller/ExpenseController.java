package com.PersonalExpense.controller;

import com.PersonalExpense.dto.ExpenseRequest;
import com.PersonalExpense.dto.ExpenseResponse;
import com.PersonalExpense.mapper.ExpenseMapper;
import com.PersonalExpense.model.Expense;
import com.PersonalExpense.service.AuthService;
import com.PersonalExpense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping()
    public ExpenseResponse addExpense(@Valid @RequestBody ExpenseRequest request){
        Expense expense = ExpenseMapper.toEntity(request);
        return ExpenseMapper.toResponse(expenseService.add(expense));
    }

    @GetMapping("{expenseId}/name")
    public String getExpenseNameById(@PathVariable Long expenseId){
        return expenseService.getItemNameByItemId(expenseId);

    }

    @GetMapping("{expenseId}/description")
    public String getExpenseDescriptionById(@PathVariable Long expenseId){
            return expenseService.getItemDescriptionByItemId(expenseId);
    }

    @GetMapping("{expenseId}/amount")
    public BigDecimal getExpenseAmountById(@PathVariable Long expenseId){
            return expenseService.getItemAmountByItemId(expenseId);
    }

    @PutMapping("{expenseId}/update")
    public ExpenseResponse updateExpense(@PathVariable Long expenseId, @Valid @RequestBody ExpenseRequest request){
        Expense expense = ExpenseMapper.toEntity(request);
        return ExpenseMapper.toResponse(expenseService.updateItem(expenseId, expense));
    }

    @DeleteMapping("{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteItem(expenseId);
        return ResponseEntity.noContent().build();
    }


}
