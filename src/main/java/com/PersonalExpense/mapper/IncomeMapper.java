package com.PersonalExpense.mapper;

import com.PersonalExpense.dto.IncomeRequest;
import com.PersonalExpense.dto.IncomeResponse;
import com.PersonalExpense.model.Income;

public class IncomeMapper {
    public static Income toEntity(IncomeRequest request){
        Income income = new Income();

        income.setName(request.getName());
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setSource(request.getSource());
        income.setDate(request.getDate());
        return income;
    }

    public static IncomeResponse toResponse(Income income){
        return new IncomeResponse(
                income.getId(),
                income.getName(),
                income.getAmount(),
                income.getDescription(),
                income.getDate(),
                income.getSource()
        );
    }
}
