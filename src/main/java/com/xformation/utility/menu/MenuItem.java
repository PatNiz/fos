package com.xformation.utility.menu;

public class MenuItem extends MenuComponent {
    public MenuItem(String title) {
        super(title);
    }
    public void execute() {
        System.out.println("execute");
    }
}
