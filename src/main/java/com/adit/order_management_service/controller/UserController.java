package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.UserResponseDto;
import com.adit.order_management_service.model.request.UserRequest;
import com.adit.order_management_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/v2")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/public/create")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequest userRequest) {
        UserResponseDto response = userService.createUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> userLogin(@RequestBody UserRequest userRequest) {
       UserResponseDto userResponseDto = userService.getUser(userRequest);
       if (Objects.nonNull(userResponseDto)) {
           return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
       }
       return new ResponseEntity<>(userRequest.getUserName() +" not found!", HttpStatus.NOT_FOUND);
    }
}
