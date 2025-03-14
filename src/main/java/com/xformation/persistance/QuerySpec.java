package com.xformation.persistance;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class QuerySpec <T extends Persistable> {
    private String tableName;
    @Getter
    private Class<T> entityType;

    private Map<String, Object> conditions;


    public QuerySpec(Class<T> entityType) {
        this.entityType = entityType;
        this.tableName = entityType.getSimpleName().toLowerCase();
        this.conditions = new HashMap<>();
    }

    public void addCondition(String column, Object value){
        conditions.put(column,value);
    }


    public String createSQLSelectQuery() {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(tableName);

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            int index = 0;
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                queryBuilder.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
                if (index < conditions.size() - 1) {
                    queryBuilder.append(" AND ");
                }
                index++;
            }
        }
        return queryBuilder.toString();
    }
}
