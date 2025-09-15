package com.adit.order_management_service.security.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
