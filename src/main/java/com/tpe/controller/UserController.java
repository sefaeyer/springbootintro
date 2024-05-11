package com.tpe.controller;

import com.tpe.domain.User;
import com.tpe.dto.UserRequest;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/register") // http://localhost:8080/register
public class UserController {

    @Autowired
    private UserService userService;

    //Register() ******************************************************
    @PostMapping() // http://localhost:8080/register + POST + JSON
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest){ // UserRequest User in DTO class i
        userService.saveUser(userRequest);
        String myResponse = "User Resgistered Successfully";
        return new ResponseEntity<>(myResponse, HttpStatus.CREATED);
    }



}
