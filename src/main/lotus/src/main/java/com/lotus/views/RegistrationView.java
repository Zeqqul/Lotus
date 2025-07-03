package com.lotus.views;

import com.lotus.handler.ViewHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationView {
    private JPanel mainPanel;
    private ViewHandler viewHandler;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton backButton;

    public RegistrationView(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        emailField = new JTextField(20);
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");
        
        // Style components
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        registerButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(60, 179, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("👤 Patient Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Main Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Username
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Username: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);
        row++;
        
        // Password
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        row++;
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Confirm Password: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(confirmPasswordField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        row++;
        
        // Name
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Full Name: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nameField, gbc);
        row++;
        
        // Phone
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(phoneField, gbc);
        row++;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Required fields note
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        noteLabel.setForeground(Color.GRAY);
        gbc.gridy = row + 1;
        formPanel.add(noteLabel, gbc);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.loadView("loginView");
            }
        });
    }
    
    private void handleRegistration() {
        // Validate required fields
        if (!validateFields()) {
            return;
        }
        
        // Get field values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // Simple registration logic for demo
        // In a real application, this would connect to the database
        JOptionPane.showMessageDialog(mainPanel,
            "Registration successful!\n\n" +
            "Username: " + username + "\n" +
            "Name: " + name + "\n" +
            "Email: " + email + "\n\n" +
            "You can now log in with your credentials.",
            "Registration Complete",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate back to login
        viewHandler.loadView("loginView");
    }
    
    private boolean validateFields() {
        // Check required fields
        if (usernameField.getText().trim().isEmpty()) {
            showError("Username is required.");
            usernameField.requestFocus();
            return false;
        }
        
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (password.isEmpty()) {
            showError("Password is required.");
            passwordField.requestFocus();
            return false;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showError("Email is required.");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(emailField.getText().trim())) {
            showError("Please enter a valid email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (nameField.getText().trim().isEmpty()) {
            showError("Full name is required.");
            nameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
