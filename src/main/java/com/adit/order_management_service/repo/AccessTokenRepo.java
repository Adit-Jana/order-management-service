package com.adit.order_management_service.repo;

import com.adit.order_management_service.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepo extends JpaRepository<AccessToken, Long> {
    List<AccessToken> findAllByUsername(String username);
    Optional<AccessToken> findByToken(String token);
}
