package com.xformation.action;

import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.model.RestaurantOrder;
import com.xformation.model.Role;
import com.xformation.persistance.repository.OrderRepository;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.DishView;
import com.xformation.view.console.DishConsoleView;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

import static com.xformation.security.Authorization.isUserAuthorizedToPerformAction;

@Log4j2
public class OrderAction extends MenuComponent implements Action {
    private final Config config;
    private final FosUser fosUser;

    public OrderAction(String title, Config config, FosUser fosUser) {
        super(title);
        this.config = config;
        this.fosUser = fosUser;
    }

    @Override
    public void execute() {
        DishView dishView = config.getDishView();
        OrderRepository orderRepository = config.getOrderRepository();

        Optional<Runnable> action = switch (dishView) {
            case DishConsoleView consoleView -> {
                if (isUserAuthorizedToPerformAction(fosUser, config, getAllowedRoles())) {
                    yield Optional.of(() -> {
                        RestaurantOrder order = consoleView.order(fosUser, config);
                        order.setUserId(fosUser.getId());
                        orderRepository.save(order);
                        System.out.println("Order for client: " + fosUser.getId() + " created");
                        log.info("Order for client: " + fosUser.getId() + " created");
                    });
                } else {
                    log.warn("User {} is not authorized to create an order", fosUser.getId());
                    yield Optional.empty();
                }
            }
            default -> {
                log.warn("Unsupported view type: {}", dishView.getClass().getSimpleName());
                yield Optional.empty();
            }
        };
        action.ifPresent(Runnable::run);
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.CLIENT);
    }
}
