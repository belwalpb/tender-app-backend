package com.tender.controller;

import com.tender.entity.User;
import com.tender.exception.InvalidRequestException;
import com.tender.model.LoginOtpRequest;
import com.tender.model.LoginOtpResponse;
import com.tender.model.LoginRequest;
import com.tender.model.LoginResponse;
import com.tender.service.UserService;
import com.tender.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add-user")
    public User addUser(@RequestBody User user) {
        // Validating User Request
        RequestValidator.validateAddUserRequest(user);
        //Saving to database.
        userService.addUser(user);
        return user;
    }

    @PostMapping("/login-using-password")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }

    // 1. OTP Send First Time.
    public LoginOtpResponse loginUsingOtp(@RequestBody LoginOtpRequest otpRequest) {
        // Validations
        if(Objects.isNull(otpRequest.getUsername())) {
            throw new InvalidRequestException("Username Must be present");
        }
        return null;
    }
    //2. OTP Resend.
    //3. OTP Verify & Token Generate



}
