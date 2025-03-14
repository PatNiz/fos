package com.xformation.action.dishActions;

import com.xformation.config.Config;
import com.xformation.model.Dish;
import com.xformation.model.FosUser;
import com.xformation.persistance.repository.DishRepository;
import com.xformation.view.console.DishConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddDishActionTest {

    @Mock
    private Config config;

    @Mock
    private DishConsoleView dishConsoleView;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private FosUser recipeCreator;

    private AddDishAction addDishAction;

    @BeforeEach
    void setUp() {
        when(config.getDishView()).thenReturn(dishConsoleView);
        when(config.getDishRepository()).thenReturn(dishRepository);
        addDishAction = new AddDishAction("Add Dish", config, recipeCreator);
    }


    @Test
    void shouldCreateDishWhenUserIsAdmin() {
        Dish dish = new Dish();
        when(recipeCreator.getId()).thenReturn(1L);
        when(dishConsoleView.createDish()).thenReturn(dish);

        try (MockedStatic<com.xformation.security.Authorization> authorizationMock =
                     mockStatic(com.xformation.security.Authorization.class)) {
            authorizationMock.when(() -> com.xformation.security.Authorization.isUserAdministrator(1L))
                    .thenReturn(true);

            addDishAction.execute();

            verify(dishRepository).save(dish);
            assertEquals(1L, dish.getRecipeCreatorId());
        }
    }


}
