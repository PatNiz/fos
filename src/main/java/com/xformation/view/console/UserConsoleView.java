package com.xformation.view.console;

import com.xformation.model.FosUser;
import com.xformation.view.UserView;

import java.util.List;
import java.util.Scanner;

public class UserConsoleView extends ConsolePrintHelper implements UserView {

    private final Scanner sc = new Scanner(System.in);
    @Override
    public void display(List<FosUser> fosUsers) {
        if (fosUsers.isEmpty()) {
            printLine("List is empty");
        } else {
            fosUsers.forEach(document -> printLine(document.toString()));
        }
    }
    @Override
    public void display(FosUser fosUser) {
        printLine(fosUser.toString());
    }


    public String chooseUser() {
        System.out.println("Choose user mode");
        System.out.println("(1 - Client, 2 - Administrator):");

        while (true) {
            var userType = readUserInput();
            if (userType != null) {
                return switch (userType) {
                    case 1 -> "Client";
                    case 2 -> "Administrator";
                    default -> throw new IllegalStateException("Unexpected value: " + userType);
                };
            }
        }
    }
    private Integer readUserInput() {
        try {
            int input = Integer.parseInt(sc.nextLine().trim());
            if (input == 1 || input == 2) {
                return input;
            } else {
                System.out.println("Invalid choice. Please select 1 or 2.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1 or 2).");
            return null;
        }
    }


}