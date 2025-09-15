package com.adit.order_management_service.specification;

import com.adit.order_management_service.entity.ProductEntity;
import com.adit.order_management_service.model.request.filter.ProductFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecification implements Specification<ProductEntity>{

    private final ProductFilterDto productFilterDto;

    public ProductSpecification(ProductFilterDto productFilterDto) {
        this.productFilterDto = productFilterDto;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        //list of predicates
        List<Predicate> predicates = new ArrayList<>();

        if (productFilterDto.getMinPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("productPrice"),
                    productFilterDto.getMinPrice()));
        }
        if (productFilterDto.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("productPrice"), productFilterDto.getMaxPrice()));
        }

        /*if (productFilterDto.getCategory() != null) {
            predicates.add(criteriaBuilder.equal(root.get("product_category"), productFilterDto.getCategory()));
        }*/

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    /*public static Specification<ProductEntity> withFilters() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if ()
        }
    }*/
}
