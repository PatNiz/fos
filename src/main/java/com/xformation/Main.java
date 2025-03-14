package com.xformation;

import com.xformation.action.LoginAction;
import com.xformation.config.Config;

public class Main {
    public static void main(String[] args) {
        Config config = new Config()
                .withSqlPersistence()
                .withConsoleView();
        LoginAction loginAction = new LoginAction("Login",config);
        loginAction.execute();
    }
}