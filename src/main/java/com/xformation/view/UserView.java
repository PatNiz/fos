package com.xformation.view;


import com.xformation.model.FosUser;

import java.util.List;

public interface UserView {
    void display(List<FosUser> fosUsers);
    void display(FosUser fosUser);
}