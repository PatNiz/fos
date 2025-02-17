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

        int userType = sc.nextInt();
        sc.nextLine();

        return switch (userType) {
            case 1 -> "Client";
            case 2 -> "Administrator";
            default -> {
                System.out.println("Invalid choice. Please select 1 or 2.");
                yield chooseUser();
            }
        };
    }

}