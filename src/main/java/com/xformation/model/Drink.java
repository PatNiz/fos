package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Drink extends Orderable implements Persistable {
    public Drink(String name, Double price) {
        super(name, price);
    }


    @Override
    public String toString() {
        return "Drink{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
