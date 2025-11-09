package com.adit.order_management_service.service;

import com.adit.order_management_service.entity.OmsRole;
import com.adit.order_management_service.entity.UserEntity;
import com.adit.order_management_service.repo.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String[] roles = user.getUserRoles().stream()
                .map(OmsRole::getRoleName)
                .toArray(String[]::new);

        return User.builder()
                .username(user.getUserName())
                .password(user.getUserPassword())
                .disabled(Boolean.FALSE.equals(user.getUserEnabled()))
                .roles(roles)
                .build();
    }

}
