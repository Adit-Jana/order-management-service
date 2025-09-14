package com.adit.order_management_service.model.request.pagination;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class PaginationFilter {
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    //private String sortBy;

    public PaginationFilter(Page<? extends Object> on) {
        this.pageNumber = on.getNumber();
        this.pageSize = on.getSize();
        this.totalElements = on.getTotalElements();
        this.totalPages = on.getTotalPages();
        this.isFirst = on.isFirst();
        this.isLast = on.isLast();
    }
}
