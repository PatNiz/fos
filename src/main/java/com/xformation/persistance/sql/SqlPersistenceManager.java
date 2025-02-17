package com.xformation.persistance.sql;

import com.xformation.persistance.Persistable;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.QuerySpec;

import java.util.List;

public class SqlPersistenceManager implements PersistenceManager {
    @Override
    public Long insert(Persistable persistable) {
        return SqlService.insert(persistable);
    }

    @Override
    public void update(Persistable persistable)  {
        SqlService.update(persistable);
    }
    @Override
    public void delete(Persistable persistable) {
        SqlService.delete(persistable);
    }
    @Override
    public <T extends Persistable> List<T> find(QuerySpec<T> querySpec) {
        return SqlService.find(querySpec);
    }
}
