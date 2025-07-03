package com.lotus.views;

import com.lotus.handler.ViewHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView {
    private JPanel mainPanel;
    private ViewHandler viewHandler;

    public HomeView(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
    }
    
    private void setupLayout() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(" Lotus Healthcare System - Patient Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    viewHandler.loadView("loginView");
                }
            }
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Main Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to your Patient Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(welcomeLabel, gbc);
        
        // Quick action buttons
        JButton viewDoctorsBtn = new JButton(" View All Doctors");
        JButton bookAppointmentBtn = new JButton(" Book Appointment");
        JButton viewBookingsBtn = new JButton(" My Bookings");
        JButton settingsBtn = new JButton(" Settings");
        
        // Style buttons
        Color buttonColor = new Color(70, 130, 180);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Dimension buttonSize = new Dimension(200, 50);
        
        JButton[] buttons = {viewDoctorsBtn, bookAppointmentBtn, viewBookingsBtn, settingsBtn};
        for (JButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
        }
        
        // Add action listeners
        viewDoctorsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "View Doctors functionality coming soon!");
        });
        
        bookAppointmentBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Book Appointment functionality coming soon!");
        });
        
        viewBookingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "My Bookings functionality coming soon!");
        });
        
        settingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Settings functionality coming soon!");
        });
        
        // Add buttons to grid
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(viewDoctorsBtn, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        contentPanel.add(bookAppointmentBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(viewBookingsBtn, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        contentPanel.add(settingsBtn, gbc);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        JLabel footerLabel = new JLabel("© 2025 Lotus Healthcare System - Patient Portal");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
