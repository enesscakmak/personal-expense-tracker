package com.PersonalExpense.service;

import com.PersonalExpense.common.InvalidCredentialsException;
import com.PersonalExpense.common.MissingInformationException;
import com.PersonalExpense.common.UserAlreadyExistsException;
import com.PersonalExpense.common.UserNotFoundException;
import com.PersonalExpense.model.User;
import com.PersonalExpense.repository.ExpenseRepository;
import com.PersonalExpense.repository.IncomeRepository;
import com.PersonalExpense.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       ExpenseRepository expenseRepository, IncomeRepository incomeRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String getUserNameById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            return user.get().getName();
        }
        else {
            throw new UserNotFoundException("User doesn't exist.");
        }
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long currentUserId, User user){
        if (user.getName() == null || user.getName().isBlank()) {
            throw new MissingInformationException("Name is mandatory!");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new MissingInformationException("E-mail is mandatory!");
        }
        User storedUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        if (!storedUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail()))
        {
            throw new UserAlreadyExistsException("E-mail already in use.");
        }
        storedUser.setName(user.getName());
        storedUser.setEmail(user.getEmail());
        return userRepository.save(storedUser);
    }

    public void changePassword(Long currentUserId, String oldPassword, String newPassword){
        User storedUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        if (!passwordEncoder.matches(oldPassword, storedUser.getPassword())){
            throw new InvalidCredentialsException("Wrong password.");
        }

        storedUser.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(storedUser);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "totalAmount", key = "'ExpenseService:' + #userId"),
            @CacheEvict(value = "totalAmount", key = "'IncomeService:' + #userId")
    })
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)){
            expenseRepository.deleteByUserId(userId);
            incomeRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);
            return true;
        }
        else{
            return false;
        }
    }
}
