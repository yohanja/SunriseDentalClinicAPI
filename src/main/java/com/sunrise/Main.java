package com.sunrise;

import com.sunrise.util.DBConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("Connection object is ready to use.");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
