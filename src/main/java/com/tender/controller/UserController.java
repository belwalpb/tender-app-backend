package com.tender.controller;

import com.tender.entity.User;
import com.tender.service.UserService;
import com.tender.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

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

}
