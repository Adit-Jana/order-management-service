package com.adit.order_management_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "oms_user_roles")
public class OmsUserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userRoleSequence")
    @SequenceGenerator(name = "userRoleSequence",
            sequenceName = "oms_user_roles_sequence",
            allocationSize = 1)
    @Column(name = "oms_user_roles_id")
    private Long omsUserRolesId;

    @Column(name = "oms_user_id")
    private Long omsUserId;

    @Column(name = "oms_role_id")
    private Long omsRoleId;
}
