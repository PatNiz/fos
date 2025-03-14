package com.xformation.action.drinkActions;

import com.xformation.action.Action;
import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.console.DrinkConsoleView;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.xformation.security.Authorization.isUserAuthorizedToPerformAction;

@Log4j2
public class GetListOfDrinksAction extends MenuComponent implements Action {
    private final Config config;
    private final FosUser fosUser;

    public GetListOfDrinksAction(String title, Config config, FosUser fosUser) {
        super(title);
        this.config = config;
        this.fosUser = fosUser;
    }

    @Override
    public void execute() {
        Optional<Runnable> action = switch (config.getDrinkView()) {
            case DrinkConsoleView consoleView -> {
                if (isUserAuthorizedToPerformAction(fosUser, config, getAllowedRoles())) {
                    yield Optional.of(() -> consoleView.display(config.getDrinkRepository().findAllDrinks(), fosUser));
                } else {
                    log.warn("User {} is not authorized to display drinks", fosUser.getId());
                    yield Optional.empty();
                }
            }
            default -> {
                log.warn("Unsupported view type: {}", config.getDrinkView().getClass().getSimpleName());
                yield Optional.empty();
            }
        };
        action.ifPresent(Runnable::run);
    }


    @Override
    public List<Role> getAllowedRoles() {
        return Arrays.asList(Role.CLIENT, Role.ADMINISTRATOR);
    }
}

