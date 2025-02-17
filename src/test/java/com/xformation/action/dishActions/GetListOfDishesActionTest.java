package com.xformation.action.dishActions;

import com.xformation.config.Config;
import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.persistance.repository.DishRepository;
import com.xformation.persistance.repository.RolesRepository;
import com.xformation.view.console.DishConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetListOfDishesActionTest {

    @Mock
    private Config config;

    @Mock
    private FosUser fosUser;

    @Mock
    private DishConsoleView consoleView;

    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private GetListOfDishesAction action;

    @BeforeEach
    void setUp() {
        action = new GetListOfDishesAction("Get dishs", config, fosUser);
        when(config.getRolesRepository()).thenReturn(rolesRepository);
    }

    @Test
    void execute_whenUserAuthorized_shouldDisplaydishs() {
        when(config.getDishView()).thenReturn(consoleView);
        when(rolesRepository.find(fosUser)).thenReturn(List.of(Role.CLIENT));
        when(config.getDishRepository()).thenReturn(mock(DishRepository.class));
        when(config.getDishRepository().findAllDishes()).thenReturn(List.of());

        action.execute();

        verify(consoleView, times(1)).display(anyList(), eq(fosUser));
    }

    @Test
    void execute_whenUserNotAuthorized_shouldNotDisplaydishs() {
        when(config.getDishView()).thenReturn(consoleView);
        when(rolesRepository.find(fosUser)).thenReturn(List.of());

        action.execute();

        verify(consoleView, never()).display(anyList(), any());
    }


}
