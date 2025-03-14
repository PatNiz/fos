package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Dish extends Orderable implements Persistable {
    public Long recipeCreatorId;
    public String cuisine;

    public Dish(String name, Double price, String cuisine) {
        super(name, price);
        this.cuisine = cuisine;
    }


    @Override
    public String toString() {
        return String.format("Dish{id=%d, recipeCreatorId=%d, cuisine='%s', price=%.2f, name='%s'}",
                id, recipeCreatorId, cuisine, price, name);
    }

    public String toStringForClient() {
        return String.format("Dish{id=%d, name='%s', cuisine='%s', price=%.2f}",
                id, name, cuisine, price);
    }
}
