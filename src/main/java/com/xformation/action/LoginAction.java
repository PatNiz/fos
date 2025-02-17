package com.xformation.action;

import com.xformation.config.Config;
import com.xformation.model.Role;
import com.xformation.model.FosUser;
import com.xformation.persistance.repository.UserRepository;
import com.xformation.utility.menu.Menu;
import com.xformation.utility.menu.MenuComponent;
import com.xformation.view.UserView;
import com.xformation.view.console.MainMenu;
import com.xformation.view.console.UserConsoleView;
import lombok.extern.log4j.Log4j2;
import com.xformation.security.Authorization;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class LoginAction extends MenuComponent implements Action {
    private final Config config;

    public LoginAction(String title, Config config) {
        super(title);
        this.config = config;
    }
    public void execute() {
        String username = "";
        UserView userView = config.getUserView();
        UserRepository userRepository = config.getUserRepository();
        if(userView instanceof UserConsoleView view){
            username = view.chooseUser();
        }
        if (Authorization.isValidLogin(username)) {
            Optional<FosUser> userByUsername = userRepository.findUserByUsername(username);
            log.info("Logged in as: " + userByUsername.get().getUsername());
            Menu menu = MainMenu.getMainMenu(userByUsername.get(), config);
            menu.chooseOption(new Scanner(System.in));
        } else {
            log.info("Invalid login or password. Try again.");
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return Arrays.asList(Role.CLIENT, Role.ADMINISTRATOR);
    }

}
