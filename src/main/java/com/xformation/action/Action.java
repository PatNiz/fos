package com.xformation.action;

import com.xformation.model.Role;

import java.util.List;

public interface Action {
    String getTitle();

    void execute();

    List<Role> getAllowedRoles();

}
