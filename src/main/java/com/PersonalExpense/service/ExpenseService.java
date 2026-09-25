package com.PersonalExpense.service;

import com.PersonalExpense.common.NotFoundException;
import com.PersonalExpense.model.Category;
import com.PersonalExpense.model.Expense;
import com.PersonalExpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExpenseService extends TransactionService<Expense> {

    private final ExpenseRepository expenseRepository;

    @Lazy
    @Autowired
    private ExpenseService self;

    public ExpenseService(AuthService authService, ExpenseRepository expenseRepository){
        super(authService, expenseRepository);
        this.expenseRepository = expenseRepository;
    }

    public Page<Expense> getExpensesByUserId(Long userId, LocalDate startDate, LocalDate endDate, Category category, Pageable pageable) {
        if (category == null) {
            return getItemsByUserId(userId, startDate, endDate, pageable);
        }
        return expenseRepository.findByUserIdWithFilters(userId, startDate, endDate, category, pageable);
    }

    public BigDecimal getTotalAmountOfCategoryByUserId(Long userId, Category category){
        if (category == null) {
            return self.getTotalItemAmountByUserId(userId);
        }
        return expenseRepository.getTotalAmountByUserIdAndCategory(userId, category);
    }

    @Override
    protected void copySpecificFields(Expense source, Expense target) {
        target.setCategory(source.getCategory());
    }

    @Override
    protected RuntimeException notFoundException(Long expenseId){
        return new NotFoundException("Expense not found"+ expenseId);
    }

}
