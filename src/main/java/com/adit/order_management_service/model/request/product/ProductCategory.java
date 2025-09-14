package com.adit.order_management_service.model.request.product;

import lombok.Getter;

@Getter
public enum ProductCategory {

    FURNITURE(1, "All types of furniture"),
    ELECTRONICS(2, "All types of electronic items"),
    FASHION(3, "All type fashion items");

    private final Integer productCategoryId;
    private final String getProductCategoryDesc;


    ProductCategory(Integer productCategoryId, String getProductCategoryDesc) {
        this.productCategoryId = productCategoryId;
        this.getProductCategoryDesc = getProductCategoryDesc;
    }

    public static ProductCategory getProductCategory(int productCategoryId) {

        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getProductCategoryId() == productCategoryId) {
                return productCategory;
            }
        }
        throw new IllegalArgumentException("Unknown category ID: " + productCategoryId);
    }
}
