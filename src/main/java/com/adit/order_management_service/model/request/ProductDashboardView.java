package com.adit.order_management_service.model.request;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.model.request.pagination.PaginationFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDashboardView {
    private List<ProductResponseDto> productResponseDto;
    private PaginationFilter paginationFilter;


    public ProductDashboardView(Page<ProductResponseDto> productResponseDto
            /*PaginationFilter paginationFilter*/) {
        this.productResponseDto = productResponseDto.getContent();
        this.paginationFilter = new PaginationFilter(productResponseDto);
        //this.paginationFilter = paginationFilter;
        // Explicitly set all pagination values from the Page object
        /*this.pageNumber = productResponseDto.getNumber();
        this.pageSize = productResponseDto.getSize();
        this.totalElements = productResponseDto.getTotalElements();
        this.totalPages = productResponseDto.getTotalPages();
        this.isFirst = productResponseDto.isFirst();
        this.isLast = productResponseDto.isLast();
        this.sortBy = sortBy; // Pass the sort information from the service*/
    }
}
