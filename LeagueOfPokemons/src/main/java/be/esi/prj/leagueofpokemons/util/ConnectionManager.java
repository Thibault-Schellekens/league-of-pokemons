package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.db.repository.RepositoryException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static Connection connection;
    private static Properties properties = null;

    private static Properties loadProperties(){
        if (properties == null){
            properties = new Properties();
            System.out.println("LoadProperties checkpoint 1");
            try (FileInputStream input = new FileInputStream("database.properties")) {
                System.out.println("LoadProperties checkpoint 2");
                properties.load(input);
                System.out.println(properties.getProperty("db.url"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } ;
        }
        return properties;
    }
    public static Connection getConnection() {
        if (connection == null) {
            try {
                loadProperties();
                String url = properties.getProperty("db.url");
                System.out.println(url);
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Fermeture impossible", ex);
        }
    }
}