package com.sunrise.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {

    }
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = "jdbc:mysql://127.0.0.1:3306/sunrise_dental_clinic";
                String username = "root";
                String password = "Yohan@002470";

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connected successfully!");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
