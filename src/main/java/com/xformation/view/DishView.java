package com.xformation.view;

import com.xformation.model.Dish;
import com.xformation.model.FosUser;

import java.util.List;

public interface DishView {
    void display(List<Dish> dishes, FosUser fosUser);

    void display(Dish dish);
}
