package com.xformation.action.drinkActions;

import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.persistance.repository.DrinkRepository;
import com.xformation.persistance.repository.RolesRepository;
import com.xformation.view.console.DrinkConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetListOfDrinksActionTest {

    @Mock
    private Config config;

    @Mock
    private FosUser fosUser;

    @Mock
    private DrinkConsoleView consoleView;

    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private GetListOfDrinksAction action;

    @BeforeEach
    void setUp() {
        action = new GetListOfDrinksAction("Get Drinks", config, fosUser);
        when(config.getRolesRepository()).thenReturn(rolesRepository);
    }

    @Test
    void execute_whenUserAuthorized_shouldDisplayDrinks() {
        when(config.getDrinkView()).thenReturn(consoleView);
        when(rolesRepository.find(fosUser)).thenReturn(List.of(Role.CLIENT));
        when(config.getDrinkRepository()).thenReturn(mock(DrinkRepository.class));
        when(config.getDrinkRepository().findAllDrinks()).thenReturn(List.of());

        action.execute();

        verify(consoleView, times(1)).display(anyList(), eq(fosUser));
    }

    @Test
    void execute_whenUserNotAuthorized_shouldNotDisplayDrinks() {
        when(config.getDrinkView()).thenReturn(consoleView);
        when(rolesRepository.find(fosUser)).thenReturn(List.of());

        action.execute();

        verify(consoleView, never()).display(anyList(), any());
    }


}
