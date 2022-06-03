package com.tender.service.impl;

import com.tender.constants.TenderConstants;
import com.tender.entity.Otp;
import com.tender.entity.TempUser;
import com.tender.entity.User;
import com.tender.exception.InvalidRequestException;
import com.tender.model.*;
import com.tender.repository.OtpRepository;
import com.tender.repository.TempUsersRepository;
import com.tender.repository.UserRepository;
import com.tender.service.UserService;
import com.tender.utils.EmailUtils;
import com.tender.utils.JwtUtils;
import com.tender.utils.OtpUtils;
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

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    TempUsersRepository tempUsersRepository;

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

    public LoginSignUpOtpResponse sendLoginOtp(LoginOtpRequest request) {
        LoginSignUpOtpResponse res=  new LoginSignUpOtpResponse();
        //1. Check if user is present or not.
        // select * from table where email = :username or mobile= :username
        Optional<User> userOptional = userRepository.findByEmailOrMobile(request.getUsername(),request.getUsername());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            //1. Generate OTP
            String otp = OtpUtils.get6DigitOtp();

            //2. Save Otp Data into DB
            Otp otpDbo = new Otp();
            otpDbo.setOtp(otp);
            otpDbo.setUserId(user.getUserId());
            otpDbo.setOtpId(UUID.randomUUID().toString());
            otpDbo.setCreatedAt(LocalDateTime.now());
            otpDbo.setValid(true);
            otpDbo.setResendAttempts(1);
            otpDbo.setVerificationAttempts(0);
            otpDbo.setOtpType(1);
            otpRepository.saveAndFlush(otpDbo);


            // 3. Send OTP Over Email
            EmailUtils.sendLoginOtp(otp,user);

            //4. Return OTP id to frontend/user
            res.setOtpId(otpDbo.getOtpId());
            res.setMessage("Otp Sent Successfully.");
            res.setStatus(true);
            return res;
        }
        else {
            res.setStatus(false);
            res.setMessage("User not exists. Please Sign up First.");
            return res;
        }
    }

    public LoginSignUpOtpResponse resendLoginOtp(String otpID) {
        LoginSignUpOtpResponse res=  new LoginSignUpOtpResponse();
        // Step-1 Try to find-out any otp data
        Optional<Otp> otpOptional = otpRepository.findById(otpID);

        if(otpOptional.isPresent()) {
            Otp otpData = otpOptional.get();

            // Validate
            //1. Check If otp is already verified.
            if(!otpData.getValid()) {
                res.setStatus(false);
                res.setMessage("Otp Is Expired");
                return res;
            }
            //2. Check If Resend Attempts are >=3 or not
            if(otpData.getResendAttempts() >=3) {
                res.setStatus(false);
                res.setMessage("Otp already sent 3 times.");
                return res;
            }

            // 3. Check if otp has been generated more then 10 minutes ago or not.
            long totalSecondsPassed = OtpUtils.getPastSeconds(otpData.getCreatedAt());
            if(totalSecondsPassed > TenderConstants.OTP_VALIDITY_TIME) {
                res.setStatus(false);
                res.setMessage("Otp is Expired. Please generate a fresh OTP");
                return res;
            }

            User user = userRepository.findById(otpData.getUserId()).get();
            // Send Otp
            EmailUtils.sendLoginOtp(otpData.getOtp(), user);

            // Increase Otp Resend Attempts.
            otpData.setResendAttempts(otpData.getResendAttempts()+1);
            otpRepository.saveAndFlush(otpData);
            res.setStatus(true);
            res.setMessage("Otp Resent Successfully.");
            return res;
        }
        else {
            res.setStatus(false);
            res.setMessage("No Otp Exists With Provided Otp ID");
            return res;
        }
     }


     public LoginSignUpOtpResponse verifyLoginOtp(String otpID, String otp) {
         LoginSignUpOtpResponse res=  new LoginSignUpOtpResponse();

         Optional<Otp> otpOptional = otpRepository.findById(otpID);

         if(otpOptional.isPresent()) {
             Otp otpData = otpOptional.get();
             //1. Check If otp is already verified.
             if(!otpData.getValid()) {
                 res.setStatus(false);
                 res.setMessage("Otp Is Expired");
                 return res;
             }


             // 2. Check if otp has been generated more then 10 minutes ago or not.
             long totalSecondsPassed = OtpUtils.getPastSeconds(otpData.getCreatedAt());
             if(totalSecondsPassed > TenderConstants.OTP_VALIDITY_TIME) {
                 res.setStatus(false);
                 res.setMessage("Otp is Expired. Please generate a fresh OTP");
                 return res;
             }

             // 3. Check Verification Attempts are 3 or not.
             if(otpData.getVerificationAttempts() >=3) {
                 res.setStatus(false);
                 res.setMessage("Otp is Compromised. Please generate a fresh OTP");
                 return res;
             }

             //4. Match Otp
             if(otpData.getOtp().equals(otp)) {

                 //Set Otp Invalid
                 otpData.setValid(false);
                 otpRepository.saveAndFlush(otpData);

                 // Generate Token
                 User user = userRepository.findById(otpData.getUserId()).get();
                 res.setStatus(true);
                 res.setMessage("Otp Verified Successfully");
                 res.setToken(JwtUtils.generateToken(user));
                 return res;
             }
             else {
                 otpData.setVerificationAttempts(otpData.getVerificationAttempts() + 1);
                 otpRepository.saveAndFlush(otpData);

                 res.setStatus(false);
                 res.setMessage("Incorrect Otp.");
                 return res;
             }
         }
         else {
             res.setStatus(false);
             res.setMessage("No Otp Exists With Provided Otp ID");
             return res;
         }
     }


     public LoginSignUpOtpResponse signUpInit(SignUpRequest request) {
        // 1. Check If that email or mobile already exist or not.
         Optional<User> userOptional = userRepository.findByEmailOrMobile(request.getEmail(), request.getMobile());
         if(userOptional.isPresent()) {
             throw new InvalidRequestException("User Already Present");
         }
         //Otherwise Proceed...........
         //1. Generate OTP
         String otp = OtpUtils.get6DigitOtp();

         //2. Save User Data to DB.
         String newUserID = UUID.randomUUID().toString();
         TempUser tempUser = new TempUser();
         tempUser.setUserId(newUserID);
         tempUser.setEmail(request.getEmail());
         tempUser.setMobile(request.getMobile());
         tempUser.setName(request.getName());
         tempUser.setRole(request.getRole());
         tempUser.setCreatedAt(LocalDateTime.now());
         tempUser.setPassword(encoder.encode(request.getPassword()));
         tempUsersRepository.saveAndFlush(tempUser);

         //3. Save Otp Data into DB
         Otp otpDbo = new Otp();
         otpDbo.setOtp(otp);
         otpDbo.setUserId(newUserID);
         otpDbo.setOtpId(UUID.randomUUID().toString());
         otpDbo.setCreatedAt(LocalDateTime.now());
         otpDbo.setValid(true);
         otpDbo.setResendAttempts(1);
         otpDbo.setVerificationAttempts(0);
         otpDbo.setOtpType(2);
         otpRepository.saveAndFlush(otpDbo);

         //4. Send Email to User.
         EmailUtils.sendSignUpOtp(otp,tempUser);

         LoginSignUpOtpResponse res = new LoginSignUpOtpResponse();
         res.setOtpId(otpDbo.getOtpId());
         res.setMessage("Otp Sent Successfully!");
         res.setStatus(true);
         return res;
     }

}
