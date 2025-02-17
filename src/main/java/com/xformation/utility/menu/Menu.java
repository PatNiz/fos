package com.xformation.utility.menu;


import lombok.extern.log4j.Log4j2;

import java.util.InputMismatchException;
import java.util.Scanner;

@Log4j2
public class Menu extends MenuComponent {


    public Menu(String title) {
        super(title);
    }

    private void printMenuComponents() {
        for (MenuComponent menuComponent : menuComponents) {
            System.out.println((menuComponents.indexOf(menuComponent) + 1) + "." + menuComponent.getTitle());
        }
        System.out.println("\nEnter your choice: ");
    }

    public void chooseOption(Scanner scanner) {
        Menu currentMenu = this;
        boolean running = true;
        while (running) {
            currentMenu.getNameWithSeparator();
            currentMenu.printMenuComponents();
            try {
                int choice = scanner.nextInt();
                var selectedOption = currentMenu.getChild(choice - 1);
                switch (selectedOption) {
                    case Menu menu -> currentMenu = menu;
                    case MenuComponent component when "Back to main menu".equals(component.getTitle()) ->
                            currentMenu = this;
                    case MenuComponent component when "Exit".equals(component.getTitle()) -> running = false;
                    case MenuComponent component -> component.execute();
                }
            } catch (InputMismatchException e) {
                log.warn("Invalid choice. Please enter a valid number.");
                scanner.nextLine();
            } catch (IndexOutOfBoundsException e) {
                log.warn("Invalid choice. Please enter a valid number.");
            }
        }
    }

    public void getNameWithSeparator() {
        System.out.println("\n" + getTitle());
        System.out.println("---------------------");
    }
}
