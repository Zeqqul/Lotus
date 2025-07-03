package com.lotus.handler;

import com.lotus.application.ApplicationWindow;
import com.lotus.views.LoginView;
import com.lotus.views.HomeView;
import com.lotus.views.RegistrationView;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

public class ViewHandler {
    private ApplicationWindow appWindow;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, JPanel> viewRegistry;

    public ViewHandler(ApplicationWindow appWindow) {
        this.appWindow = appWindow;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);
        this.viewRegistry = new HashMap<>();
        
        // Initialize the main panel in the application window
        appWindow.add(mainPanel);
        
        // Initialize all views
        initializeViews();
    }

    private void initializeViews() {
        // Create and register all views
        try {
            // Login View
            LoginView loginView = new LoginView(this);
            registerView("loginView", loginView.getPanel());
            
            // Home View
            HomeView homeView = new HomeView(this);
            registerView("homeView", homeView.getPanel());
            
            // Registration View
            RegistrationView registrationView = new RegistrationView(this);
            registerView("registrationView", registrationView.getPanel());
            
            System.out.println("✅ All views initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Error initializing views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerView(String viewName, JPanel viewPanel) {
        viewRegistry.put(viewName, viewPanel);
        mainPanel.add(viewPanel, viewName);
        System.out.println("📋 Registered view: " + viewName);
    }

    /**
     * Loads a view based on the view name.
     * 
     * @param viewName The name of the view to load.
     */
    public void loadView(String viewName) {
        this.loadView(viewName, null);
    }

    /**
     * Loads a view based on the view name and passes data to it.
     * 
     * @param viewName The name of the view to load.
     * @param data     The data to pass to the view.
     */
    public void loadView(String viewName, Object data) {
        if (viewRegistry.containsKey(viewName)) {
            cardLayout.show(mainPanel, viewName);
            appWindow.setTitle("Lotus Healthcare System - " + viewName);
            //debugging
         // System.out.println("🔄 Loaded view: " + viewName);
            
            if (data != null) {
                System.out.println(" With data: " + data.toString());
            }
        } else {
            System.err.println(" View not found: " + viewName);
            System.err.println("Available views: " + viewRegistry.keySet());
        }
    }
    
    public ApplicationWindow getAppWindow() {
        return appWindow;
    }
}
