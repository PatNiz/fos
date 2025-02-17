package com.xformation.view.console;

import com.xformation.model.Drink;
import com.xformation.model.FosUser;
import com.xformation.view.DrinkView;

import java.util.List;
import java.util.Scanner;

public class DrinkConsoleView extends ConsolePrintHelper implements DrinkView {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void display(List<Drink> drinks, FosUser fosUser) {
        if (drinks.isEmpty()) {
            printLine("List is empty");
        } else {
            drinks.forEach(drink -> printLine(drink.toString()));

        }
    }

    @Override
    public void display(Drink drink) {
        printLine(drink.toString());
    }

    public Drink createDrink() {
        printLine("Drink name: ");
        String name = sc.nextLine();

        printLine("Price: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price:");
            sc.next();
        }
        Double price = sc.nextDouble();
        sc.nextLine();
        return new Drink(name, price);
    }
}
