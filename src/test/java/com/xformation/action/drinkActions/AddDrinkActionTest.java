package com.xformation.action.drinkActions;

import com.xformation.config.Config;
import com.xformation.model.Drink;
import com.xformation.model.FosUser;
import com.xformation.persistance.repository.DrinkRepository;
import com.xformation.view.console.DrinkConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddDrinkActionTest {

    @Mock
    private Config config;

    @Mock
    private DrinkConsoleView drinkConsoleView;

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private FosUser recipeCreator;

    private AddDrinkAction addDrinkAction;

    @BeforeEach
    void setUp() {
        when(config.getDrinkView()).thenReturn(drinkConsoleView);
        when(config.getDrinkRepository()).thenReturn(drinkRepository);
        addDrinkAction = new AddDrinkAction("Add Drink", config, recipeCreator);
    }


    @Test
    void shouldCreateDishWhenUserIsAdmin() {
        Drink drink = new Drink();
        when(recipeCreator.getId()).thenReturn(1L);
        when(drinkConsoleView.createDrink()).thenReturn(drink);

        try (MockedStatic<com.xformation.security.Authorization> authorizationMock =
                     mockStatic(com.xformation.security.Authorization.class)) {
            authorizationMock.when(() -> com.xformation.security.Authorization.isUserAdministrator(1L))
                    .thenReturn(true);

            addDrinkAction.execute();

            verify(drinkRepository).save(drink);
        }
    }


}
