package com.xformation.view.console;

import com.xformation.action.OrderAction;
import com.xformation.action.dishActions.*;
import com.xformation.action.drinkActions.AddDrinkAction;
import com.xformation.action.drinkActions.GetListOfDrinksAction;
import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.utility.menu.Menu;
import com.xformation.utility.menu.MenuItem;

import static com.xformation.security.Authorization.isUserAdministrator;

public class MainMenu {
    public static Menu getMainMenu(FosUser fosUser, Config config) {
        Menu mainMenu = new Menu("Main menu");
        if (isUserAdministrator(fosUser.getId())) {
            mainMenu.add(getDishAdminMenu(fosUser, config));
            mainMenu.add(getDrinkAdminMenu(fosUser, config));
            } else {
            mainMenu.add(getDishMenu(fosUser, config));
            mainMenu.add(getDrinkMenu(fosUser, config));
            mainMenu.add(new OrderAction("Order",config,fosUser));
        }
        return mainMenu;
    }
    public static Menu getDishMenu(FosUser fosUser, Config config) {
        Menu dishMenu = new Menu("Dish Menu");
        dishMenu.add(new GetListOfDishesAction("List of dishes", config, fosUser));
        dishMenu.add(new MenuItem("Back to main menu"));
        return dishMenu;
    }
    public static Menu getDrinkAdminMenu(FosUser fosUser, Config config) {
        Menu drinkAdminMenu = new Menu("Drink Admin Menu");
        drinkAdminMenu.add(new AddDrinkAction("Add drink", config, fosUser));
        drinkAdminMenu.add(new GetListOfDrinksAction("List of drinks", config, fosUser));
        drinkAdminMenu.add(new MenuItem("Back to main menu"));
        return drinkAdminMenu;
    }
    public static Menu getDrinkMenu(FosUser fosUser, Config config) {
        Menu drinkAdminMenu = new Menu("Drink Menu");
        drinkAdminMenu.add(new GetListOfDrinksAction("List of drinks", config, fosUser));
        drinkAdminMenu.add(new MenuItem("Back to main menu"));
        return drinkAdminMenu;
    }

    public static Menu getDishAdminMenu(FosUser fosUser, Config config) {
        Menu dishAdminMenu = new Menu("Dish Admin Menu");
        dishAdminMenu.add(new AddDishAction("Add dish", config, fosUser));
        dishAdminMenu.add(new GetListOfDishesAction("List of dishes", config, fosUser));
        dishAdminMenu.add(new MenuItem("Back to main menu"));
        return dishAdminMenu;
    }
}
