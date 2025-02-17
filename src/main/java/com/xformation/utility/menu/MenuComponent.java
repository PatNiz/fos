package com.xformation.utility.menu;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuComponent {
    @Getter
    protected final String title;
    List<MenuComponent> menuComponents = new ArrayList<>();



    protected MenuComponent(String title) {
        this.title = title;
    }


    public void add(MenuComponent menuComponent) {
        menuComponents.add(menuComponent);
    }


    public void remove(MenuComponent menuComponent) {
        menuComponents.remove(menuComponent);
    }

    public MenuComponent getChild(int index) {
        return menuComponents.get(index);
    }

    protected void execute() {
    }
}