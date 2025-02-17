package com.xformation.action.dishActions;

import com.xformation.action.Action;
import com.xformation.model.Role;
import com.xformation.utility.menu.MenuComponent;

import java.util.List;

public class DeleteDishAction extends MenuComponent implements Action {

    protected DeleteDishAction(String title) {
        super(title);
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public List<Role> getAllowedRoles() {
        //TODO
        return List.of();
    }

}
