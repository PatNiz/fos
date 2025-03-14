package com.xformation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Orderable {
    Long id;
    String name;
    Double price;

    public Orderable(String name, Double price) {
        this.name = name;
        this.price = price;
    }

}
