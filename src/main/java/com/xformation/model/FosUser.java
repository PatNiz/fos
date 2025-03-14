package com.xformation.model;

import com.xformation.persistance.Persistable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FosUser implements Persistable {
    private Long id;
    private String username;

    public FosUser(String username) {
        this.username = username;
    }
}