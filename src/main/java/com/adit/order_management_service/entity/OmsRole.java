package com.adit.order_management_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "oms_roles")
public class OmsRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "user_role_sequence")
    @SequenceGenerator(name = "user_role_sequence",
            sequenceName = "oms_role_id_sequence",
            allocationSize = 1)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;
}
