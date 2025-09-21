package com.adit.order_management_service.repo;

import com.adit.order_management_service.constant.Roles;
import com.adit.order_management_service.entity.OmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmsRoleRepo extends JpaRepository<OmsRole, Long> {
    Optional<OmsRole> findByRoleName(String roleName);
}
