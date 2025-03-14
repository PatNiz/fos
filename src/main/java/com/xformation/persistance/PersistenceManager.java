package com.xformation.persistance;

import java.util.List;

public interface PersistenceManager {


    Long insert(Persistable persistable);


    void update(Persistable persistable);

    void delete(Persistable persistable);

    <T extends Persistable> List<T> find(QuerySpec<T> querySpec);
}