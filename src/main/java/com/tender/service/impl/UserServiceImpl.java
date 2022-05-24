package com.tender.service.impl;

import com.tender.constants.TenderConstants;
import com.tender.entity.User;
import com.tender.model.LoginOtpRequest;
import com.tender.model.LoginOtpResponse;
import com.tender.model.LoginRequest;
import com.tender.model.LoginResponse;
import com.tender.repository.UserRepository;
import com.tender.service.UserService;
import com.tender.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

   @Autowired
   BCryptPasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public User addUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        user.setCurrentStatus(true);
        user.setCreatedAt(LocalDateTime.now());
        String plainPassword = user.getPassword();
        String encryptedPassword = encoder.encode(plainPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        user.setPassword(JwtUtils.generateToken(user));
        return user;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        LoginResponse res = new LoginResponse();
        //Verify User exist or not.
        Optional<User> userOptional = userRepository.findByEmailOrMobile(request.getUsername(),request.getUsername());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isPasswordMatched = encoder.matches(request.getPassword(),user.getPassword());
            if(isPasswordMatched) {
                // JWT Generate For User
                res.setStatus(true);
                res.setBearerToken(JwtUtils.generateToken(user));
                res.setMessage("Login Successful");
                return res;
            }
            else {
                res.setStatus(false);
                res.setMessage("Invalid Username or Password");
                return res;
            }
        }
        else {
            res.setStatus(false);
            res.setMessage(TenderConstants.NO_USER_EXISTS_MSG);
            return res;
        }
    }

    public LoginOtpResponse loginUsingOtp(LoginOtpRequest request) {
        LoginOtpResponse res=  new LoginOtpResponse();
        //1. Check if user is present or not.
        Optional<User> userOptional = userRepository.findByEmailOrMobile(request.getUsername(),request.getUsername());
        if(userOptional.isPresent()) {
            //1. Generate OTP
            //2. Save Otp Data into DB && Send OTP Over Email.
            //3. Return oTP id to frontend/user


        }
        else {
            res.setStatus(false);
            res.setMessage("User not exists");
            return res;
        }
    }

}
