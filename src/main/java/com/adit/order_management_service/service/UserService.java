package com.adit.order_management_service.service;

import com.adit.order_management_service.constant.Roles;
import com.adit.order_management_service.dto.response.UserResponseDto;
import com.adit.order_management_service.entity.OmsRole;
import com.adit.order_management_service.entity.UserEntity;
import com.adit.order_management_service.model.request.UserRequest;
import com.adit.order_management_service.repo.OmsRoleRepo;
import com.adit.order_management_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OmsRoleRepo omsRoleRepo;

    /*@Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;*/

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(UserRequest userRequest) {

        // check if username already exists

        Set<OmsRole> userRoleList;

        List<Roles> userRoleTagList;


        // fetch role from DB
        userRoleList = userRequest.getRole().stream()
                .map(roleName -> omsRoleRepo.findByRoleName(roleName.getDesc())
                        .orElseThrow(() -> new RuntimeException(" Role not found: " + roleName.getDesc())))
                .collect(Collectors.toSet());

        userRoleTagList = userRequest.getRole().stream().collect(Collectors.toList());

        UserEntity userEntity = userRepo.save(UserEntity.builder()
                .userName(userRequest.getUserName())
                .userPassword(passwordEncoder.encode(userRequest.getPassword()))
                .userRolesTag(userRoleTagList)
                .userRoles(userRoleList)
                .build());

        return UserResponseDto.builder()
                .userId(userEntity.getUserId())
                .userName(userRequest.getUserName())
                .rolesList(userRoleList.stream()
                        .map(r -> r.getRoleName())
                        .collect(Collectors.toList()))
                .build();
    }

    public UserResponseDto getUser(UserRequest userRequest) {

        // This triggers your CustomUserDetailsService.loadUserByUsername
        // User is authenticated now
        UserDetails userDetails = loadUserByUsername(userRequest.getUserName());

        UserEntity omsUser = userRepo.findByUserName(userRequest.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userRequest.getUserName()));

        UserResponseDto userResponseDto = null;

        if (Objects.nonNull(userDetails) && Objects.nonNull(userDetails.getUsername())) {
            userResponseDto = UserResponseDto.builder()
                    .userId(omsUser.getUserId())
                    .userName(userDetails.getUsername())
                    .rolesList(omsUser.getUserRoles().stream()
                            .map(OmsRole::getRoleDesc)
                            .collect(Collectors.toList()
                    ))
                    .build();
        }
        return userResponseDto;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity omsUser = userRepo.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));


        return User.builder()
                .username(omsUser.getUserName())
                .password(omsUser.getUserPassword()) // must already be BCrypt encoded
                .disabled(Boolean.FALSE.equals(omsUser.getUserEnabled()))
                .roles(String.valueOf(omsUser.getUserRolesTag().stream()
                        .map(Roles::getDesc).collect(Collectors.toSet())))    // Spring will add ROLE_ prefix internally
                .build();

    }
}
