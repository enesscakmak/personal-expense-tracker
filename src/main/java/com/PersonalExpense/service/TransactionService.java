package com.PersonalExpense.service;

import com.PersonalExpense.common.AccessDeniedException;
import com.PersonalExpense.common.MissingInformationException;
import com.PersonalExpense.common.NotFoundException;
import com.PersonalExpense.model.Transaction;
import com.PersonalExpense.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionService<T extends Transaction> {
    protected final AuthService authService;
    private final TransactionRepository<T> transactionRepository;

    public TransactionService(AuthService authService, TransactionRepository<T> transactionRepository) {
        this.authService = authService;
        this.transactionRepository = transactionRepository;
    }

    @CacheEvict(value = "totalAmount", key = "#root.targetClass.simpleName + ':' + @authService.getCurrentUserId()")
    public T add(T item) {
        item.setUserId(authService.getCurrentUserId());
        return transactionRepository.save(item);
    }

    public String getItemNameByItemId(Long itemId) {
        T item = transactionRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw notFoundException(itemId);
        }
        if (isOwnedByCurrentUser(itemId)) {
            return item.getName();
        }
        throw accessDeniedException();
    }

    public String getItemDescriptionByItemId(Long itemId) {
        T item = transactionRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw notFoundException(itemId);
        }
        if (isOwnedByCurrentUser(itemId)) {
            return item.getDescription();
        }
        throw accessDeniedException();
    }

    public BigDecimal getItemAmountByItemId(Long itemId) {
        T item = transactionRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw notFoundException(itemId);
        }
        if (isOwnedByCurrentUser(itemId)) {
            return item.getAmount();
        }
        throw accessDeniedException();
    }

    @CacheEvict(value = "totalAmount", key = "#root.targetClass.simpleName + ':' + @authService.getCurrentUserId()")
    public T updateItem(Long itemId, T item) {
        T storedItem = transactionRepository.findById(itemId).orElse(null);
        if (storedItem == null){
            throw notFoundException(itemId);
        }
        if (item.getName().isBlank()) {
            throw missingInformationException();
        }
        if (isOwnedByCurrentUser(itemId)) {
            storedItem.setName(item.getName());
            storedItem.setAmount(item.getAmount());
            storedItem.setDescription(item.getDescription());
            storedItem.setDate(item.getDate());
            copySpecificFields(item, storedItem);
            transactionRepository.save(storedItem);
            return storedItem;
        }
        throw accessDeniedException();
    }

    @CacheEvict(value = "totalAmount", key = "#root.targetClass.simpleName + ':' + @authService.getCurrentUserId()")
    public void deleteItem(Long itemId) {
        T item = transactionRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw notFoundException(itemId);
        }
        if (isOwnedByCurrentUser(itemId)) {
            transactionRepository.delete(item);
            return;
        }
        throw accessDeniedException();
    }

    public Page<T> getItemsByUserId(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable){
        return transactionRepository.findByUserIdWithinDateRange(userId, startDate, endDate, pageable);
    }

    public Long getUserIdByItemId(Long itemId) {
        T item = transactionRepository.findById(itemId).orElseThrow(() -> notFoundException(itemId));
        return item.getUserId();
    }

    @Cacheable(value = "totalAmount", key = "#root.targetClass.simpleName + ':' + #userId")
    public BigDecimal getTotalItemAmountByUserId(Long userId){
        List<T> allItems = transactionRepository.findByUserId(userId);
        BigDecimal totalAmount = new BigDecimal(0);
        for (T item : allItems){
            totalAmount = totalAmount.add(item.getAmount());
        }
        return totalAmount;
    }

    public boolean isOwnedByCurrentUser(Long itemId){
        return getUserIdByItemId(itemId).equals(authService.getCurrentUserId());
    }

    protected void copySpecificFields(T source, T target) {}

    protected RuntimeException notFoundException(Long itemId) {
        return new NotFoundException("Item not found." + itemId);
    }

    protected RuntimeException accessDeniedException(){
        return new AccessDeniedException("You don't have the necessary permission for that.");
    }

    protected RuntimeException missingInformationException(){
        return new MissingInformationException("Necessary information not provided.");
    }
}
