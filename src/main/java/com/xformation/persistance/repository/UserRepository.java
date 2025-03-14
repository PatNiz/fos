package com.xformation.persistance.repository;

import com.xformation.model.FosUser;
import com.xformation.persistance.Persistable;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.QuerySpec;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final PersistenceManager manager;
    public UserRepository(PersistenceManager manager){
        this.manager = manager;
    }

    public void save(FosUser fosUser){
        manager.insert(fosUser);
    }
    public void delete(FosUser user){
        manager.delete(user);
    }
    public List<FosUser> findAllUsers() {
        return manager.find(new QuerySpec<>(FosUser.class));
    }
    public Optional<FosUser> findUserByUsername(String username){
        QuerySpec querySpec = new QuerySpec(FosUser.class);
        querySpec.addCondition("username",username);
        List<Persistable> persistables = manager.find(querySpec);
        return persistables.isEmpty() ? Optional.empty() : Optional.of((FosUser) persistables.get(0));
    }

}
