package com.quiz.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * DBConnection reads db.properties and provides Connection.
 */
public class DBConnection {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties p = new Properties();
            p.load(is);
            url = p.getProperty("db.url");
            user = p.getProperty("db.user");
            password = p.getProperty("db.password");
        } catch (Exception e) {
            System.err.println("Failed to load db.properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        if (url == null) throw new IllegalStateException("DB URL is not configured.");
        return DriverManager.getConnection(url, user, password);
    }
}