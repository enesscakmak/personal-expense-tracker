package com.PersonalExpense.service;

import com.PersonalExpense.common.InvalidCredentialsException;
import com.PersonalExpense.common.TooManyLoginAttemptsException;
import com.PersonalExpense.common.UserAlreadyExistsException;
import com.PersonalExpense.dto.AuthResponse;
import com.PersonalExpense.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenRevocationService tokenRevocationService;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    public AuthService(PasswordEncoder passwordEncoder, UserService userService, JwtService jwtService, TokenRevocationService tokenRevocationService, RefreshTokenService refreshTokenService, LoginAttemptService loginAttemptService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
        this.tokenRevocationService = tokenRevocationService;
        this.refreshTokenService = refreshTokenService;
        this.loginAttemptService = loginAttemptService;
    }

    public boolean isPasswordCorrect(String rawPassword, String hashedPassword){
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public boolean doesEmailExists(String email){
        return userService.getUserByEmail(email) != null;
    }
    
    public AuthResponse login(String email, String password){
        if (loginAttemptService.isBlocked(email)){
            throw new TooManyLoginAttemptsException("Too many failed login attempts. Try again later.");
        }
        User user = userService.getUserByEmail(email);
        if (user == null || !isPasswordCorrect(password, user.getPassword())){
            loginAttemptService.recordFailedAttempt(email);
            throw new InvalidCredentialsException("Invalid password or e-mail.");
        }
        loginAttemptService.resetAttempts(email);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), jwtService.generateToken(user.getId()), refreshTokenService.create(user.getId()));
        }


    public User register(User user){
        if (doesEmailExists(user.getEmail())){
            throw new UserAlreadyExistsException("Mail already in use.");
        }
        return userService.addUser(user);
    }

    public void logout(String refreshToken){
        revokeCurrentToken();
        refreshTokenService.delete(refreshToken);
    }

    public void revokeCurrentToken(){
        tokenRevocationService.revoke(getCurrentToken());
    }

    public void logoutAllDevices(){
        revokeCurrentToken();
        refreshTokenService.deleteAllForUser(getCurrentUserId());
    }

    public Long getCurrentUserId(){
        return (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }

    public String getCurrentToken(){
        String token = (String) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getDetails();
        return token;
    }

    public AuthResponse refreshToken(String token){
        Long userId = refreshTokenService.getUserIdAndDeleteToken(token);
        User user = userService.getUserById(userId);

        return new AuthResponse(userId, user.getName(), user.getEmail(), jwtService.generateToken(userId), refreshTokenService.create(userId));
    }


}
