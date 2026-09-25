package com.PersonalExpense.service;

import com.PersonalExpense.common.NotFoundException;
import com.PersonalExpense.model.Income;
import com.PersonalExpense.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class IncomeService extends TransactionService<Income> {

    private final IncomeRepository incomeRepository;

    @Lazy
    @Autowired
    private IncomeService self;

    public IncomeService(AuthService authService, IncomeRepository incomeRepository){
        super(authService, incomeRepository);
        this.incomeRepository = incomeRepository;
    }

    public Page<Income> getIncomesByUserId(Long userId, LocalDate startDate, LocalDate endDate, String source, Pageable pageable) {
        if (source == null) {
            return getItemsByUserId(userId, startDate, endDate, pageable);
        }
        return incomeRepository.findByUserIdWithFilters(userId, startDate, endDate, source, pageable);
    }

    public BigDecimal getTotalAmountOfSourceByUserId(Long userId, String source){
        if (source == null) {
            return self.getTotalItemAmountByUserId(userId);
        }
        return incomeRepository.getTotalAmountByUserIdAndSource(userId, source);
    }

    @Override
    protected void copySpecificFields(Income source, Income target) {
        target.setSource(source.getSource());
    }

    @Override
    protected RuntimeException notFoundException(Long incomeId){
        return new NotFoundException("Income not found" + incomeId);
    }

}
