package com.PersonalExpense.mapper;

import com.PersonalExpense.dto.UserRequest;
import com.PersonalExpense.dto.UserResponse;
import com.PersonalExpense.dto.UserUpdateRequest;
import com.PersonalExpense.model.User;

public class UserMapper {
    public static User toEntity(UserRequest request){
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public static User toEntity(UserUpdateRequest request){
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
