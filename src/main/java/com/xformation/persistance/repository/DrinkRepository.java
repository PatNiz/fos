package com.xformation.persistance.repository;

import com.xformation.model.Drink;
import com.xformation.persistance.Persistable;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.QuerySpec;

import java.util.List;
import java.util.Optional;

public class DrinkRepository {
    private final PersistenceManager manager;

    public DrinkRepository(PersistenceManager manager) {
        this.manager = manager;
    }

    public void save(Drink drink) {
        manager.insert(drink);
    }

    public void delete(Drink drink) {
        manager.delete(drink);
    }

    public List<Drink> findAllDrinks() {
        return manager.find(new QuerySpec<>(Drink.class));
    }

}
