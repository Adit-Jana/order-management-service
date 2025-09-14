package com.adit.order_management_service.model.request.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDto {
    private Double minPrice;
    private Double maxPrice;
    private String category;
}
