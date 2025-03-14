package com.xformation.view;

import com.xformation.model.Drink;
import com.xformation.model.FosUser;

import java.util.List;

public interface DrinkView {
    void display(List<Drink> drinks, FosUser fosUser);

    void display(Drink drink);
}