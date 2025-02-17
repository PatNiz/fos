package com.xformation.view.console;

import com.xformation.config.Config;
import com.xformation.model.*;
import com.xformation.persistance.repository.DishRepository;
import com.xformation.persistance.repository.DrinkRepository;
import com.xformation.view.DishView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xformation.security.Authorization.isUserAdministrator;

public class DishConsoleView extends ConsolePrintHelper implements DishView {

    private final Scanner sc = new Scanner(System.in);
    @Override
    public void display(List<Dish> dishes, FosUser fosUser) {
        if (dishes.isEmpty()) {
            printLine("List is empty");
        } else {
            if (isUserAdministrator(fosUser.getId())) {
                dishes.forEach(dish -> printLine(dish.toString()));
            } else if (!isUserAdministrator(fosUser.getId())){
                dishes.forEach(dish -> printLine(dish.toStringForClient()));
            }
        }
    }
    @Override
    public void display(Dish dish) {
        printLine(dish.toString());
    }

    public Dish createDish() {
        printLine("Dish name: ");
        String name = sc.nextLine();

        printLine("Price: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price:");
            sc.next();
        }
        Double price = sc.nextDouble();
        sc.nextLine();
        String cuisine = CuisineChooserHelper.chooseCuisine().toString();
        return new Dish(name, price, cuisine);
    }

    public RestaurantOrder order(FosUser fosUser, Config config) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome! Select the dishes you want to order.");

        List<Dish> availableDishes = getAvailableDishes(config, fosUser);
        List<Long> dishIds = selectItems(scanner, availableDishes, "dish");

        List<Drink> availableDrinks = getAvailableDrinks(config, fosUser);
        List<Long> drinkIds = selectItems(scanner, availableDrinks, "drink");

        return new RestaurantOrder(dishIds, drinkIds);
    }

    private List<Dish> getAvailableDishes(Config config, FosUser fosUser) {
        System.out.println("Select cuisine you want to order: ");
        DishRepository dishRepository = config.getDishRepository();
        List<Dish> dishesByCuisine = dishRepository.findDishesByCuisine(
                CuisineChooserHelper.chooseCuisine().toString()
        );

        if (dishesByCuisine.isEmpty()) {
            System.out.println("No dishes available for the selected cuisine.");
        } else {
            config.getDishView().display(dishesByCuisine, fosUser);
        }

        return dishesByCuisine;
    }

     private List<Drink> getAvailableDrinks(Config config,FosUser fosUser) {
        DrinkRepository drinkRepository = config.getDrinkRepository();
        List<Drink> drinks = drinkRepository.findAllDrinks();

        if (drinks.isEmpty()) {
            System.out.println("No drinks available.");
        } else {
            System.out.println("Available drinks:");
            config.getDrinkView().display(drinks, fosUser);
        }

        return drinks;
    }

    private <T extends Orderable> List<Long> selectItems(Scanner scanner, List<T> availableItems, String itemType) {
        List<Long> selectedIds = new ArrayList<>();
        Set<Long> availableIds = availableItems.stream().map(T::getId).collect(Collectors.toSet());

        if (availableIds.isEmpty()) {
            return selectedIds;
        }

        System.out.printf("Add %ss (or type 'finish' to end):%n", itemType);

        while (true) {
            System.out.print("Enter " + itemType + " ID: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("finish")) {
                break;
            }

            try {
                Long id = Long.parseLong(input);

                if (availableIds.contains(id)) {
                    selectedIds.add(id);
                    System.out.println(itemType + " added.");
                } else {
                    System.out.println("Invalid ID, please select from the available " + itemType + "s.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid " + itemType + " ID.");
            }
        }

        return selectedIds;
    }



}