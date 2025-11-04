package com.adit.order_management_service.controller;

import com.adit.order_management_service.constant.Roles;
import com.adit.order_management_service.dto.response.UserResponseDto;
import com.adit.order_management_service.entity.OmsRole;
import com.adit.order_management_service.entity.RefreshToken;
import com.adit.order_management_service.entity.UserEntity;
import com.adit.order_management_service.model.request.UserRequest;
import com.adit.order_management_service.repo.AccessTokenRepo;
import com.adit.order_management_service.repo.OmsRoleRepo;
import com.adit.order_management_service.repo.RefreshTokenRepo;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/v2/auth")
public class AuthenticationController {

    // todo
    // oms access token table needs to be created in database
    // sequence needs to be added


    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final OmsRoleRepo roleRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AccessTokenRepo accessTokenRepo;

    public AuthenticationController(AuthenticationManager authManager, CustomUserDetailsService userDetailsService, JwtService jwtService, UserRepo userRepo, PasswordEncoder passwordEncoder, OmsRoleRepo roleRepo, RefreshTokenRepo refreshTokenRepo, AccessTokenRepo accessTokenRepo) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.accessTokenRepo = accessTokenRepo;
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
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String oldRefreshToken = authHeader.substring(7);
        if (!jwtService.isValid(oldRefreshToken) || !jwtService.isRefreshToken(oldRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        String refreshToken = authHeader.substring(7).trim();
        Optional<RefreshToken> optionalToken = refreshTokenRepo.findByToken(refreshToken);

        if (optionalToken.isEmpty()) {
            System.out.println("Refresh token not found in DB: " + refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found");
        }

        RefreshToken tokenEntity = optionalToken.get();

        if (tokenEntity.isRevoked()) {
            System.out.println("Refresh token is revoked: " + refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token has been revoked");
        }

        if (tokenEntity.getExpiry().isBefore(Instant.now())) {
            System.out.println("Refresh token is expired: " + refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token has expired");
        }

        // Revoke the old token
        tokenEntity.setRevoked(true);
        refreshTokenRepo.save(tokenEntity);

        // ✅ Revoke all previous access tokens for this user
        String username = jwtService.extractUsername(oldRefreshToken);
        accessTokenRepo.findAllByUsername(username).forEach(t -> {
            t.setRevoked(true);
            accessTokenRepo.save(t);
        });



        // Issue new tokens
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            refreshTokenRepo.findByToken(refreshToken).ifPresent(token -> {
                token.setRevoked(true);
                refreshTokenRepo.save(token);
            });
        }
        return ResponseEntity.noContent().build();
    }

}
