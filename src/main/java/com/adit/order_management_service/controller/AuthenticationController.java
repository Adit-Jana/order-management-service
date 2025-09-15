package com.adit.order_management_service.controller;

import com.adit.order_management_service.security.model.LoginRequest;
import com.adit.order_management_service.security.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v2/auth")
public class AuthenticationController {

    @PostMapping(value = "/get_auth_token")
    public ResponseEntity<?> getAuthenticationToken(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        String role = "admin".equals(username) ? "ROLE_ADMIN" : "ROLE_USER";

        if (("admin".equals(username) && "password".equals(password)) ||
                ("user".equals(username) && "password".equals(password))) {
            String token = JwtUtil.generateToken(username, role);
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
