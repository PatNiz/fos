package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.Data;

@Data
public class OrderDrinks implements Persistable {
    private Long id;
    private Long orderId;
    private Long drinkId;

    public OrderDrinks(Long orderId, Long drinkId) {
        this.orderId = orderId;
        this.drinkId = drinkId;
    }
}
