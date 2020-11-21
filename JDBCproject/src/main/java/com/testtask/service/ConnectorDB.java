package com.testtask.service;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class ConnectorDB {

    private final Connection connection;

    public ConnectorDB() throws SQLException, FileNotFoundException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException("Property file is not found");
        }
        this.connection = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
    }

    public Connection getConnection() {
        return connection;
    }
}