package com.xformation.view.console;

import java.util.Scanner;

public class CuisineChooserHelper {
    private static final Scanner sc = new Scanner(System.in);

    public enum Cuisine {
        POLISH("Polish"),
        MEXICAN("Mexican"),
        ITALIAN("Italian");

        private final String name;

        Cuisine(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static Cuisine chooseCuisine() {
        System.out.println("Choose Cuisine:");
        Cuisine[] cuisines = Cuisine.values();
        for (int i = 0; i < cuisines.length; i++) {
            System.out.println((i + 1) + " - " + cuisines[i]);
        }

        int choice;
        while (true) {
            System.out.print("Enter your choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();
                if (choice >= 1 && choice <= cuisines.length) {
                    return cuisines[choice - 1];
                }
            }
            System.out.println("Invalid choice. Please select a valid option.");
        }
    }
}
