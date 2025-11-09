package com.adit.order_management_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "oms_refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "token_sequence")
    @SequenceGenerator(name = "token_sequence",
            sequenceName = "oms_refresh_token_sequence",
            allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String token;

    private String username;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant expiry;
    private boolean revoked = false;
}
