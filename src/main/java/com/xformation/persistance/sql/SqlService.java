package com.xformation.persistance.sql;

import com.xformation.model.FosUser;
import com.xformation.model.Role;
import com.xformation.persistance.Persistable;
import com.xformation.persistance.QuerySpec;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class SqlService {
    private static final String CONFIG_FILE = "database.properties";

    protected static Long insert(Persistable persistableObject) {
        try (Connection conn = getConnection()) {
            String tableName = "\"" + toSnakeCase(persistableObject.getClass().getSimpleName()) + "\"";

            List<Field> fields = extractFields(persistableObject);
            String sql = buildInsertQuery(tableName, fields);

            log.info("Executing SQL: {}", sql);

            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                mapValuesToStatement(statement, fields, persistableObject);
                statement.executeUpdate();

                return fetchGeneratedId(statement);
            }
        } catch (SQLException e) {
            log.error("SQL Error while inserting {}: {}", persistableObject.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("SQL Error while inserting record", e); //PersistenceException
        } catch (Exception e) {
            log.error("Unexpected error while inserting {}: {}", persistableObject.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("Failed to insert record", e);
        }
    }

    public static List<Field> extractFields(Persistable persistableObject) {
        List<Field> fields = new ArrayList<>();
        Class<?> targetClass = persistableObject.getClass();
        while (targetClass != null) {
            for (Field field : targetClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    continue;
                }
                if (Collection.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                fields.add(field);
            }
            targetClass = targetClass.getSuperclass();
        }
        return fields;
    }

    private static Long fetchGeneratedId(PreparedStatement statement) throws SQLException{
        try (ResultSet rs = statement.getGeneratedKeys()) {
            if (rs.next()) {
                Long id = rs.getLong(1);
                return id;
            }
            throw new SQLException("Failed to retrieve generated ID from the database.");
        }
    }

    private static String buildInsertQuery(String tableName, List<Field> fields) {
        String columnNames = fields.stream()
                .map(field -> toSnakeCase(field.getName()))
                .collect(Collectors.joining(", "));
        String placeholders = fields.stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING id", tableName, columnNames, placeholders);
    }

    private static void mapValuesToStatement(PreparedStatement statement, List<Field> fields, Persistable persistableObject) throws IllegalAccessException, SQLException {
        for (int i = 0; i < fields.size(); i++) {
            statement.setObject(i + 1, fields.get(i).get(persistableObject));
        }
    }

    public static <T extends Persistable> List<T> find(QuerySpec querySpec) {
        List<T> entities = new ArrayList<>();
        Class<T> entityType = querySpec.getEntityType();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String selectQuery = querySpec.createSQLSelectQuery();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                T entity = entityType.getDeclaredConstructor().newInstance();
                List<Field> fields = new ArrayList<>();


                Class<?> currentClass = entityType;
                while (currentClass != null) {
                    fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                    currentClass = currentClass.getSuperclass();
                }


                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = toSnakeCase(field.getName());

                    try {
                        Object fieldValue = resultSet.getObject(fieldName);
                        field.set(entity, fieldValue);
                    } catch (SQLException e) {
                        log.warn("Brak wartości dla pola: " + fieldName);
                    }
                }

                entities.add(entity);
            }
        } catch (SQLException | ReflectiveOperationException e) {
            log.error("Błąd podczas pobierania danych: " + e.getMessage(), e);
        }

        return entities;
    }

    public static void update(Persistable persistable) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            Class<?> clazz = persistable.getClass();
            String tableName = "\"" + toSnakeCase(clazz.getSimpleName()) + "\"";
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue;
                try {
                    fieldValue = field.get(persistable);
                    updateQuery.append(fieldName).append(" = '").append(fieldValue).append("', ");
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
            updateQuery.deleteCharAt(updateQuery.length() - 2);
            updateQuery.append(" WHERE id = ").append(persistable.getId());
            statement.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    protected static void delete(Persistable persistable) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            Class<?> clazz = persistable.getClass();
            String tableName = clazz.getSimpleName();
            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + persistable.getId();
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static Connection getConnection() throws SQLException {

        Properties connectionProperties = new Properties();

        try (InputStream inputStream = SqlService.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            connectionProperties.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        String host = connectionProperties.getProperty("db.host");
        String port = connectionProperties.getProperty("db.port");
        String database = connectionProperties.getProperty("db.database");
        String username = connectionProperties.getProperty("db.username");
        String password = connectionProperties.getProperty("db.password");
        String connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(connectionUrl, username, password);
    }

    public static List<Role> getUserRoles(FosUser fosUser) {
        List<Role> roles = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT role FROM user_roles WHERE user_id = ? ")) {
            preparedStatement.setLong(1, fosUser.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String roleName = resultSet.getString("role");
                    roles.add(Role.valueOf(roleName));
                }
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return roles.isEmpty() ? Collections.EMPTY_LIST : roles;
    }

    public static void saveUserRoles(Long projectId, Long userId, List<Role> roles) {
        String insertRolesQuery = "INSERT INTO user_roles (project_id, user_id, role) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertRolesQuery)) {
            for (Role role : roles) {
                preparedStatement.setLong(1, projectId);
                preparedStatement.setLong(2, userId);
                preparedStatement.setString(3, role.name());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private static String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
