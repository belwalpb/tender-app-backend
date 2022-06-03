package com.tender.service;

import com.tender.entity.User;
import com.tender.model.*;


public interface UserService {

     User addUser(User user);

     LoginResponse loginUser(LoginRequest request);

     LoginSignUpOtpResponse sendLoginOtp(LoginOtpRequest request);

     LoginSignUpOtpResponse resendLoginOtp(String otpID);

     LoginSignUpOtpResponse verifyLoginOtp(String otpID, String otp);

     LoginSignUpOtpResponse signUpInit(SignUpRequest request);


}
