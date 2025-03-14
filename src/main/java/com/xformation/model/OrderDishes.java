package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.Data;

@Data
public class OrderDishes implements Persistable {
    private Long id;
    private Long orderId;
    private Long dishId;

    public OrderDishes(Long orderId, Long dishId) {
        this.orderId = orderId;
        this.dishId = dishId;
    }
}
