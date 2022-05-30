package com.tender.service;

import com.tender.entity.User;
import com.tender.model.LoginOtpRequest;
import com.tender.model.LoginOtpResponse;
import com.tender.model.LoginRequest;
import com.tender.model.LoginResponse;


public interface UserService {

     User addUser(User user);

     LoginResponse loginUser(LoginRequest request);

     LoginOtpResponse sendLoginOtp(LoginOtpRequest request);

     LoginOtpResponse resendLoginOtp(String otpID);

     LoginOtpResponse verifyLoginOtp(String otpID, String otp);


}
