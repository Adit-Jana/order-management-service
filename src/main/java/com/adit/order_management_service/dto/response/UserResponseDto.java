package com.adit.order_management_service.dto.response;

import com.adit.order_management_service.constant.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDto {
    private Long userId;
    private String userName;
    private List<String> rolesList;
}
