package com.xformation.persistance.repository;

import com.xformation.model.Dish;
import com.xformation.persistance.Persistable;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.QuerySpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DishRepository {
    private final PersistenceManager manager;
    public DishRepository(PersistenceManager manager){
        this.manager = manager;
    }

    public void save(Dish dish){ manager.insert(dish);
    }
    public void delete(Dish dish){
        manager.delete(dish);
    }
    public List<Dish> findAllDishes() {
        return manager.find(new QuerySpec<>(Dish.class));
    }
    public List<Dish> findDishesByCuisine(String cuisine) {
        QuerySpec querySpec = new QuerySpec(Dish.class);
        querySpec.addCondition("cuisine", cuisine);

        List<Persistable> persistables = manager.find(querySpec);

        List<Dish> dishes = new ArrayList<>();
        for (Persistable persistable : persistables) {
            if (persistable instanceof Dish) {
                dishes.add((Dish) persistable);
            }
        }

        return dishes;
    }




}
