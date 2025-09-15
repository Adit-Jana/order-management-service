package com.adit.order_management_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oms_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence",
            sequenceName = "oms_product_sequence",
            allocationSize = 1)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_quantity")
    private Integer productQuantity;
}
