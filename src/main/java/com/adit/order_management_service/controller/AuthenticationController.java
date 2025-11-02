package com.adit.order_management_service.controller;

import com.adit.order_management_service.constant.Roles;
import com.adit.order_management_service.dto.response.UserResponseDto;
import com.adit.order_management_service.entity.OmsRole;
import com.adit.order_management_service.entity.UserEntity;
import com.adit.order_management_service.model.request.UserRequest;
import com.adit.order_management_service.repo.OmsRoleRepo;
import com.adit.order_management_service.repo.UserRepo;
import com.adit.order_management_service.security.dto.LoginRequest;
import com.adit.order_management_service.security.dto.TokenResponse;
import com.adit.order_management_service.security.jwt.JwtService;
import com.adit.order_management_service.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/v2/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final OmsRoleRepo roleRepo;

    public AuthenticationController(AuthenticationManager authManager, CustomUserDetailsService userDetailsService, JwtService jwtService, UserRepo userRepo, PasswordEncoder passwordEncoder, OmsRoleRepo roleRepo) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequest request) {
        if (userRepo.findByUserName(request.getUsername()).isPresent()) {
            log.info("User: {} already exist", request.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // username already exists
        }

        UserEntity user = new UserEntity();
        user.setUserName(request.getUsername());
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserEnabled(true);

        List<OmsRole> roleEntities = request.getRoles().stream()
                .map(roleName -> roleRepo.findByRoleName(java.lang.String.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .toList();

        user.setUserRolesTag(roleEntities.stream()
                .map(r -> Roles.getSpecificRoles(Math.toIntExact(r.getRoleId())))
                .collect(Collectors.toList())
        );

        UserEntity saved = userRepo.save(user);

        List<String> roles = saved.getUserRolesTag().stream()
                .map(r -> "ROLE_" + r)
                .toList();

        UserResponseDto dto = new UserResponseDto(saved.getUserId(), saved.getUserName(), roles);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // Authenticate using Spring Security
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // Load user details
        UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());

        // Generate tokens
        java.lang.String accessToken = jwtService.generateAccessToken(user);
        java.lang.String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        java.lang.String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = authHeader.substring(7);
        if (!jwtService.isValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        java.lang.String username = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        java.lang.String newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> currentUser(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity omsUser = userRepo.findByUserName(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<java.lang.String> roles = omsUser.getUserRoles().stream()
                .map(OmsRole::getRoleName)
                .map(r -> "ROLE_" + r)
                .toList();

        UserResponseDto dto = UserResponseDto.builder()
                .userId(omsUser.getUserId())
                .userName(omsUser.getUserName())
                .rolesList(roles)
                .build();

        return ResponseEntity.ok(dto);
    }
}
