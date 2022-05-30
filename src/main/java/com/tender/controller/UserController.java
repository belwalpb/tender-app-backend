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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login/password")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }

    // 1. OTP Send First Time.
    @PostMapping("/login/send-otp")
    public LoginOtpResponse sendLoginOtp(@RequestBody LoginOtpRequest otpRequest) {
        // Validations
        if(Objects.isNull(otpRequest.getUsername())) {
            throw new InvalidRequestException("Username Must be present");
        }
        return userService.sendLoginOtp(otpRequest);
    }

    //2. OTP Verify & Token Generate
    @PostMapping("/login/verify-otp")
    public Object verifyLoginOtp(@RequestParam String otpID, @RequestParam String otp) {
        return userService.verifyLoginOtp(otpID,otp);
    }

    //3. OTP Resend.
    @GetMapping("/login/resend-otp")
    public Object resendLoginOtp(@RequestParam String otpID) {
        return userService.resendLoginOtp(otpID);
    }




}
