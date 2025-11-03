package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.UserResponseDto;
import com.adit.order_management_service.entity.OmsRole;
import com.adit.order_management_service.entity.UserEntity;
import com.adit.order_management_service.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepo userRepository;

    public UserController(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        UserEntity user = userRepository.findByUserName(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getUserRoles().stream()
                .map(OmsRole::getRoleName)
                .map(r -> "ROLE_" + r)
                .toList();

        UserResponseDto dto = new UserResponseDto(user.getUserId(), user.getUserName(), roles);
        return ResponseEntity.ok(dto);
    }
}
