package com.foodtech.mate.domain.state;

import java.util.Arrays;

public enum OrderState {

    WAITING("대기중"),
    PREPARING("준비중"),
    CANCEL("취소");

    private String orderStateCode;

    OrderState(String orderStateCode) {
        this.orderStateCode = orderStateCode;
    }

    public static OrderState findByOrderState(String orderStateCode){
        return Arrays.stream(OrderState.values())
                .filter(orderState -> orderState.orderStateCode.equals(orderStateCode))
                .findAny()
                .orElse(null);
    }
}
