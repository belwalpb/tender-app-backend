package com.tender.service.impl;

import com.tender.entity.User;
import com.tender.repository.UserRepository;
import com.tender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

   @Autowired
   BCryptPasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    public User addUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        user.setCurrentStatus(true);
        user.setCreatedAt(LocalDateTime.now());
        String plainPassword = user.getPassword();
        String encryptedPassword = encoder.encode(plainPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        return user;
    }

}
