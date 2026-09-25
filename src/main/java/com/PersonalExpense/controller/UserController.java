package com.PersonalExpense.controller;

import com.PersonalExpense.dto.ExpenseResponse;
import com.PersonalExpense.dto.IncomeResponse;
import com.PersonalExpense.dto.PasswordChangeRequest;
import com.PersonalExpense.dto.UserResponse;
import com.PersonalExpense.dto.UserUpdateRequest;
import com.PersonalExpense.mapper.ExpenseMapper;
import com.PersonalExpense.mapper.IncomeMapper;
import com.PersonalExpense.mapper.UserMapper;
import com.PersonalExpense.model.Category;
import com.PersonalExpense.model.User;
import com.PersonalExpense.service.AuthService;
import com.PersonalExpense.service.ExpenseService;
import com.PersonalExpense.service.IncomeService;
import com.PersonalExpense.service.UserService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final AuthService authService;

    public UserController(UserService userService, ExpenseService expenseService, IncomeService incomeService, AuthService authService){
        this.userService = userService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.authService = authService;
    }

    @GetMapping("/name")
    public String getUserName(){
            return userService.getUserNameById(authService.getCurrentUserId());
    }

    @PutMapping("/update")
    public UserResponse updateUser(@Valid @RequestBody UserUpdateRequest request){
        User user = UserMapper.toEntity(request);
        return UserMapper.toResponse(userService.updateUser(authService.getCurrentUserId(), user));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request){
        userService.changePassword(authService.getCurrentUserId(), request.getCurrentPassword(), request.getNewPassword());
        authService.logoutAllDevices();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(){
        if (userService.deleteUser(authService.getCurrentUserId())) {
            authService.logoutAllDevices();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/expenses")
    public Page<ExpenseResponse> getExpensesByUserId(@RequestParam(required = false) LocalDate startDate,
                                                     @RequestParam(required = false) LocalDate endDate,
                                                     @RequestParam(required = false) Category category,
                                                     @ParameterObject Pageable pageable){
        return expenseService.getExpensesByUserId(authService.getCurrentUserId(), startDate, endDate, category, pageable).map(ExpenseMapper::toResponse);
    }

    @GetMapping("/incomes")
    public Page<IncomeResponse> getIncomesByUserId(@RequestParam(required = false) LocalDate startDate,
                                                   @RequestParam(required = false) LocalDate endDate,
                                                   @RequestParam(required = false) String source,
                                                   @ParameterObject Pageable pageable){
        return incomeService.getIncomesByUserId(authService.getCurrentUserId(), startDate, endDate, source, pageable).map(IncomeMapper::toResponse);
    }

    @GetMapping("/expenses/total-amount")
    public BigDecimal getTotalAmountOfCategoryByUserId(@RequestParam(required = false) Category category){
        return expenseService.getTotalAmountOfCategoryByUserId(authService.getCurrentUserId(), category);
    }

    @GetMapping("/incomes/total-amount")
    public BigDecimal getTotalAmountOfSourceByUserId(@RequestParam(required = false) String source){
        return incomeService.getTotalAmountOfSourceByUserId(authService.getCurrentUserId(), source);
    }
}
