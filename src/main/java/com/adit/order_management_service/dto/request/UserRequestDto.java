package com.adit.order_management_service.dto.request;

import com.adit.order_management_service.constant.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    private String userName;
    private String password;
    private Roles role;
}
