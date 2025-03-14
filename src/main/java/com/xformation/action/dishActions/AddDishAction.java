package com.xformation.action.dishActions;

import com.xformation.action.Action;
import com.xformation.config.Config;
import com.xformation.model.Dish;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.console.DishConsoleView;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

import static com.xformation.security.Authorization.isUserAdministrator;

@Log4j2
public class AddDishAction extends MenuComponent implements Action {
    private final Config config;
    private final FosUser recipeCreator;

    public AddDishAction(String title, Config config, FosUser recipeCreator) {
        super(title);
        this.config = config;
        this.recipeCreator = recipeCreator;
    }

    @Override
    public void execute() {
        if (!isUserAdministrator(recipeCreator.getId())) {
            log.info("User with ID {} is not an administrator and cannot create dishes.", recipeCreator.getId());
            return;
        }
        Optional.ofNullable(config.getDishView())
                .flatMap(dishView -> switch (dishView) {
                    case DishConsoleView consoleView -> Optional.of(consoleView.createDish());
                    default -> {
                        log.warn("Unsupported view type: {}", dishView.getClass().getSimpleName());
                        yield Optional.empty();
                    }
                })
                .ifPresent(this::saveDish);
    }

    private void saveDish(Dish dish) {
        dish.setRecipeCreatorId(recipeCreator.getId());
        config.getDishRepository().save(dish);
        log.info("Dish created");
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.ADMINISTRATOR);
    }
}
