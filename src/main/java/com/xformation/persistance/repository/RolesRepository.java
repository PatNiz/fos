package com.xformation.persistance.repository;

import com.xformation.model.Role;
import com.xformation.model.FosUser;
import com.xformation.persistance.sql.SqlService;
import java.util.List;

public class RolesRepository {
    public RolesRepository() {
    }

    public List<Role> find(FosUser fosUser){
        return SqlService.getUserRoles(fosUser);
    }

}
