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
    private Long product_id;
    private String product_name;
    private String product_desc;
    private String product_category;
    private Double product_price;
    private Integer product_quantity;
}
