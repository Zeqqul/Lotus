package com.lotus.application;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.text.View;

import com.lotus.configuration.Configuration;
import com.lotus.database.DBManager;
import com.lotus.handler.ViewHandler;

/**
 * The primary frame of the application. It is the main window that contains all the views
 * 
 * @author Raharth Ahmed
 */
public class ApplicationWindow extends JFrame {
    private final ViewHandler handler;

   public ApplicationWindow() {
        super(Configuration.APPLICATION_NAME);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Configuration.WINDOW_SIZE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBManager.getInstance().closeConnection();
            }
        });

        this.handler = new ViewHandler(this);
        this.handler.loadView("loginView");
    }
}
