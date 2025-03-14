package com.xformation.security;
import com.xformation.config.Config;
import com.xformation.model.Role;
import com.xformation.model.FosUser;
import com.xformation.persistance.repository.RolesRepository;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.xformation.persistance.sql.SqlService.getConnection;
@Log4j2
public class Authorization {

    public static boolean isValidLogin(String username) {
        try (Connection connection = getConnection()) {
            String query = "SELECT COUNT(*) FROM fosuser WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }
    public static boolean isUserAuthorizedToPerformAction(FosUser fosUser, Config config, List<Role> roles) {
        RolesRepository rolesRepository = config.getRolesRepository();
        List<Role> userRoles = rolesRepository.find(fosUser);
        List<Role> commonRoles = new ArrayList<>(userRoles);
        commonRoles.retainAll(roles);
        return !commonRoles.isEmpty();
    }
    public static boolean isUserAdministrator(Long userId) {
        String query = "SELECT COUNT(*) FROM administrator WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }



}