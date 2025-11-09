package com.adit.order_management_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "oms_access_tokens")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "token_sequence")
    @SequenceGenerator(name = "token_sequence",
            sequenceName = "oms_access_token_sequence",
            allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String token;

    private String username;
    private Instant expiry;
    private boolean revoked;
}
