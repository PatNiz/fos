package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.Data;

import java.util.List;
@Data
public class RestaurantOrder implements Persistable {
    Long id;
    private List<Long> dishes;
    private List<Long> drinks;
    Long userId;

    public RestaurantOrder(List<Long> dishes, List<Long> drinks) {
        this.drinks = drinks;
        this.dishes = dishes;
    }

    @Override
    public Long getId() {
        return id;
    }
}
