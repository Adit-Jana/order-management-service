package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingAddress {
    private String firstName;
    private String lastName;
    private String address1;
    private String city;
    private String country;
    private String zipCode;
}
