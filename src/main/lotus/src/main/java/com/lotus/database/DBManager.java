package com.lotus.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBManager {
    private static DBManager instance;
    private Connection connection;
    private Properties dbProperties;
    
    private DBManager() {
        loadDatabaseProperties();
        initialiseConnection();
    }
    
    // Singleton pattern to ensure only one database connection
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }
    
    // Load database properties from file
    private void loadDatabaseProperties() {
        dbProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                dbProperties.load(input);
            } else {
                throw new RuntimeException("database.properties file not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Set default values if properties file is not found
            setDefaultProperties();
        }
    }
    
    // Set default database properties
    private void setDefaultProperties() {
        dbProperties.setProperty("db.url", "jdbc:mysql://localhost:3306/lotus_healthcare");
        dbProperties.setProperty("db.username", "root");
        dbProperties.setProperty("db.password", "password");
        dbProperties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }
    
    // Initialize database connection
    private void initialiseConnection() {
        try {
            Class.forName(dbProperties.getProperty("db.driver"));
            String url = dbProperties.getProperty("db.url");
            String username = dbProperties.getProperty("db.username");
            String password = dbProperties.getProperty("db.password");
            
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully!");
        } catch (Exception e) {
            System.err.println("Failed to establish database connection:");
            e.printStackTrace();
        }
    }
    
    // Get database connection
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initialiseConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    // Test database connection and show sample data
    public void testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Testing database connection...");
                
                // Test doctors table
                testDoctorsTable();
                
                // Test patients table
                testPatientsTable();
                
                // Test bookings table
                testBookingsTable();
                
            } else {
                System.err.println("No database connection available");
            }
        } catch (SQLException e) {
            System.err.println("Error testing database connection:");
            e.printStackTrace();
        }
    }
    
    private void testDoctorsTable() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT doctor_id, name, specialization FROM doctors LIMIT 5")) {
            
            System.out.println("\n=== DOCTORS TABLE ===");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("doctor_id") + 
                                 " | Name: " + resultSet.getString("name") + 
                                 " | Specialization: " + resultSet.getString("specialization"));
            }
        } catch (SQLException e) {
            System.err.println("Error querying doctors table: " + e.getMessage());
        }
    }
    
    private void testPatientsTable() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT patient_id, name, email FROM patients LIMIT 5")) {
            
            System.out.println("\n=== PATIENTS TABLE ===");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("patient_id") + 
                                 " | Name: " + resultSet.getString("name") + 
                                 " | Email: " + resultSet.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Error querying patients table: " + e.getMessage());
        }
    }
    
    private void testBookingsTable() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as booking_count FROM bookings")) {
            
            System.out.println("\n=== BOOKINGS TABLE ===");
            while (resultSet.next()) {
                System.out.println("Total bookings: " + resultSet.getInt("booking_count"));
            }
        } catch (SQLException e) {
            System.err.println("Error querying bookings table: " + e.getMessage());
        }
    }
    
    // Close database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBManager db = DBManager.getInstance();
        db.testConnection();
        
        // Close connection when done
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.closeConnection();
        }));
    }
}