package com.adit.order_management_service.security.dto;

public record TokenResponse(String accessToken, String refreshToken) {
}
