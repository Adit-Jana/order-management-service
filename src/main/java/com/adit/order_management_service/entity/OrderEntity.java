package com.adit.order_management_service.entity;

import com.adit.order_management_service.model.request.order.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oms_order")
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "order_sequence",
            strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "order_sequence",
    sequenceName = "order_item_sequence",
    allocationSize = 1)
    private Long order_id;
    private String order_desc;
    private BigDecimal total_order_amount;
    private Long user_id;
    private String payment_uid;
    private Long billing_ref_id;
    private Long shipping_ref_id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();
}
