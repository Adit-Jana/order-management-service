package com.adit.order_management_service.entity;

import com.adit.order_management_service.constant.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oms_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "userSequence")
    @SequenceGenerator(name = "userSequence",
            sequenceName = "oms_user_sequence",
            allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_details_id")
    private Long userDetailsId;

    @Column(name = "user_enabled")
    private Boolean userEnabled;

    @Column(name = "user_roles", nullable = false)
    @ColumnDefault(value = "NA")
    private List<String> userRolesTag;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "oms_user_roles",
            joinColumns = @JoinColumn(name = "oms_user_id"),
            inverseJoinColumns = @JoinColumn(name = "oms_role_id")
    )
    private Set<OmsRole> userRoles = new HashSet<>();
}


