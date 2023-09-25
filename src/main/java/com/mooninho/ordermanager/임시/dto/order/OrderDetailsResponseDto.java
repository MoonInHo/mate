package com.mooninho.ordermanager.임시.dto.order;

import com.mooninho.ordermanager.menu.domain.vo.MenuName;
import com.mooninho.ordermanager.order.domain.vo.Quantity;
import com.mooninho.ordermanager.order.domain.vo.TotalMenuPrice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponseDto {

    private MenuName menuName;
    private Quantity quantity;
    private TotalMenuPrice totalMenuPrice;
}
