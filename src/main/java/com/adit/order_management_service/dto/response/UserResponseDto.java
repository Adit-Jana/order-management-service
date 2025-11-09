package com.adit.order_management_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String userName;
    private List<String> rolesList;
}
