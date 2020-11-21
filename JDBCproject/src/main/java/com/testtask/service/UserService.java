package com.testtask.service;

import com.testtask.bean.User;
import com.testtask.exception.DataException;

import java.sql.*;
import java.util.Optional;

/**
 * Класс для реализации api сервиса, поиска и внесения изменений в БД
 **/
public class UserService implements Service<Optional<User>> {
    private final ConnectorDB connection;

    public UserService(ConnectorDB connectorDB) {
        this.connection = connectorDB;
    }

    /**
     * Метод для поиска пользователя по логину.
     */
    public Optional<User> getAccountByFirstName(String firstName) throws DataException {
        String query = "SELECT * FROM Account WHERE firstName =?";
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(0, firstName);
            final ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                User user = new User(resultSet.getString("lastName"),
                        resultSet.getString("firstName"));
                return Optional.of(user);
            }

        } catch (SQLException ex) {
            throw new DataException(ex.getMessage(), ex);
        }

        return Optional.empty();
    }

    /**
     * Метод для изменения фамилии у заданному имени учетной записи
     */
    public void updateLastName(String firstName, String lastName) throws DataException {

        String query = "UPDATE Account SET lastName =? WHERE firstName =?";
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(0, lastName);
            statement.setString(1, firstName);
            int result = statement.executeUpdate();
            if (result < 1) {
                throw new DataException("Data is not updated");
            }
        } catch (SQLException ex) {
            throw new DataException(ex.getMessage(), ex);
        }
    }
}