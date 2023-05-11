package com.foodtech.mate.domain.wrapper.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class CustomerRequest {

    private final String customerRequest;

    private CustomerRequest(String customerRequest) {
        this.customerRequest = customerRequest;
    }

    public static CustomerRequest of(String customerRequest) {
        return new CustomerRequest(customerRequest);
    }
}
