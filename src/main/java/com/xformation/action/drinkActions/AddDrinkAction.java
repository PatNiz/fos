package com.xformation.action.drinkActions;

import com.xformation.action.Action;
import com.xformation.config.Config;
import com.xformation.model.Drink;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.console.DrinkConsoleView;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

import static com.xformation.security.Authorization.isUserAdministrator;

@Log4j2
public class AddDrinkAction extends MenuComponent implements Action {
    private final Config config;
    private final FosUser recipeCreator;

    public AddDrinkAction(String title, Config config, FosUser recipeCreator) {
        super(title);
        this.config = config;
        this.recipeCreator = recipeCreator;
    }

    @Override
    public void execute() {
        if (!isUserAdministrator(recipeCreator.getId())) {
            log.info("User with ID {} is not an administrator and cannot create drinks.", recipeCreator.getId());
            return;
        }
        Optional.ofNullable(config.getDrinkView())
                .flatMap(drinkView -> switch (drinkView) {
                    case DrinkConsoleView consoleView -> Optional.of(consoleView.createDrink());
                    default -> {
                        log.warn("Unsupported view type: {}", drinkView.getClass().getSimpleName());
                        yield Optional.empty();
                    }
                })
                .ifPresent(this::saveDrink);
    }

    private void saveDrink(Drink drink) {
        config.getDrinkRepository().save(drink);
        log.info("Drink created");
    }


    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.ADMINISTRATOR);
    }
}
