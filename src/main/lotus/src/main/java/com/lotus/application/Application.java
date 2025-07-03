package com.lotus.application;

import javax.swing.JFrame;


import com.lotus.database.DBManager;

/**
 * The primary entry point of the application - using a singleton pattern to ensure
 * a single instance of the application is created.
 * 
 * @author Raharth Ahmed
 */

public class Application {
    public static void main(String[] args) {
       DBManager.getInstance().getConnection(); 

       JFrame frame = new ApplicationWindow();
       frame.setVisible(true);
    
        // Print a message indicating the application has started
        System.out.println("🏥 Lotus Healthcare System is running...");
    }
}
