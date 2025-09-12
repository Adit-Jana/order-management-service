package com.adit.order_management_service.model.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {
    private Long userId; // later should move to dedicated user class
    private Long orderId;
    private String orderDesc;
    @JsonAlias({"items"})
    private List<Item> itemsList;
    private BillingAddress billingAddress;
    private ShippingAddress shippingAddress;
    private PaymentDetails paymentDetails;
    private String couponCode;

}
