package com.PersonalExpense.controller;

import com.PersonalExpense.dto.IncomeRequest;
import com.PersonalExpense.dto.IncomeResponse;
import com.PersonalExpense.mapper.IncomeMapper;
import com.PersonalExpense.model.Income;
import com.PersonalExpense.service.AuthService;
import com.PersonalExpense.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;
    private final AuthService authService;


    public IncomeController(IncomeService incomeService, AuthService authService) {
        this.incomeService = incomeService;
        this.authService = authService;
    }

    @PostMapping("")
    public IncomeResponse addIncome(@Valid @RequestBody IncomeRequest request){
        Income income = IncomeMapper.toEntity(request);
        return IncomeMapper.toResponse(incomeService.add(income));
    }

    @GetMapping("{incomeId}/name")
    public String getIncomeNameById(@PathVariable Long incomeId){
        return incomeService.getItemNameByItemId(incomeId);

    }

    @GetMapping("{incomeId}/description")
    public String getIncomeDescriptionById(@PathVariable Long incomeId){
        return incomeService.getItemDescriptionByItemId(incomeId);
    }

    @GetMapping("{incomeId}/amount")
    public BigDecimal getIncomeAmountById(@PathVariable Long incomeId){
        return incomeService.getItemAmountByItemId(incomeId);
    }

    @PutMapping("{incomeId}/update")
    public IncomeResponse updateIncome(@PathVariable Long incomeId, @Valid @RequestBody IncomeRequest request){
        Income income = IncomeMapper.toEntity(request);
        return IncomeMapper.toResponse(incomeService.updateItem(incomeId, income));
    }

    @DeleteMapping("{incomeId}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long incomeId){
        incomeService.deleteItem(incomeId);
        return ResponseEntity.noContent().build();
    }
}
