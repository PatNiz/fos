package com.xformation.action.dishActions;

import com.xformation.action.Action;
import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.console.DishConsoleView;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.xformation.security.Authorization.isUserAuthorizedToPerformAction;

@Log4j2
public class GetListOfDishesAction extends MenuComponent implements Action {
    private final Config config;
    private final FosUser fosUser;

    public GetListOfDishesAction(String title, Config config, FosUser fosUser) {
        super(title);
        this.config = config;
        this.fosUser = fosUser;
    }

    @Override
    public void execute() {
        Optional<Runnable> action = switch (config.getDishView()) {
            case DishConsoleView consoleView -> {
                if (isUserAuthorizedToPerformAction(fosUser, config, getAllowedRoles())) {
                    yield Optional.of(() -> consoleView.display(config.getDishRepository().findAllDishes(), fosUser));
                } else {
                    log.warn("User {} is not authorized to display dishes", fosUser.getId());
                    yield Optional.empty();
                }
            }
            default -> {
                log.warn("Unsupported view type: {}", config.getDishView().getClass().getSimpleName());
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
