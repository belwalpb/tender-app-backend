package com.tender.service;

import com.tender.entity.User;
import com.tender.model.LoginRequest;
import com.tender.model.LoginResponse;


public interface UserService {

    public User addUser(User user);

    public LoginResponse loginUser(LoginRequest request);


}
