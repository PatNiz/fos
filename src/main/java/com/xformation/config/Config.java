package com.xformation.config;

import com.xformation.action.Action;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.repository.*;
import com.xformation.persistance.sql.SqlPersistenceManager;
import com.xformation.view.DishView;
import com.xformation.view.DrinkView;
import com.xformation.view.UserView;
import com.xformation.view.console.DishConsoleView;
import com.xformation.view.console.DrinkConsoleView;
import com.xformation.view.console.UserConsoleView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Config {
    private PersistenceManager persistenceManager;
    @Getter
    private DishView dishView;
    private DrinkView drinkView;
    private DishRepository dishRepository;
    private RolesRepository rolesRepository;
    private DrinkRepository drinkRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private List<Action> actions;
    private UserView userView;

    public Config withSqlPersistence() {
        setPersistenceManager(new SqlPersistenceManager());
        setDishRepository(new DishRepository(persistenceManager));
        setUserRepository(new UserRepository(persistenceManager));
        setOrderRepository(new OrderRepository(persistenceManager));
        setDrinkRepository(new DrinkRepository(persistenceManager));
        setRolesRepository(new RolesRepository());
        return this;
    }
    public Config withConsoleView() {
        setDishView(new DishConsoleView());
        setDrinkView(new DrinkConsoleView());
        setUserView(new UserConsoleView());
        return this;
    }
}