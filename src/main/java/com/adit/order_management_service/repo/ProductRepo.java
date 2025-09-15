package com.adit.order_management_service.repo;

import com.adit.order_management_service.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
}
