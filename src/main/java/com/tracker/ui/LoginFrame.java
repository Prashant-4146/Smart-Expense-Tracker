package com.tracker.ui;

import com.tracker.dao.CreditDAO;
import com.tracker.dao.UserDAO;
import com.tracker.model.Credit;
import com.tracker.model.User;
import com.tracker.service.ExpenseTrackerService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private UserDAO userDAO;
    private CreditDAO creditDAO;

    public LoginFrame() {
        setTitle("Expense Tracker - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userDAO = new UserDAO();
        creditDAO = new CreditDAO();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createSignUpPanel(), "SignUp");

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        JButton switchBtn = new JButton("Don't have an account? Sign Up");
        switchBtn.setContentAreaFilled(false);
        switchBtn.setBorderPainted(false);
        switchBtn.setForeground(new Color(41, 128, 185));
        switchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 4;
        panel.add(switchBtn, gbc);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            User user = userDAO.authenticateUser(email, password);
            if (user != null) {
                openDashboard(user.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        });

        switchBtn.addActionListener(e -> cardLayout.show(mainPanel, "SignUp"));

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBackground(new Color(39, 174, 96));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(signUpBtn, gbc);

        JButton switchBtn = new JButton("Already have an account? Login");
        switchBtn.setContentAreaFilled(false);
        switchBtn.setBorderPainted(false);
        switchBtn.setForeground(new Color(41, 128, 185));
        switchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 5;
        panel.add(switchBtn, gbc);

        signUpBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            User user = new User(0, name, email, password);
            int userId = userDAO.registerUser(user);
            if (userId > 0) {
                // Insert initial credit for the new user
                creditDAO.insertCredit(new Credit(0, 0.0, userId));
                JOptionPane.showMessageDialog(this, "Registration successful!");
                openDashboard(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Email might already exist.");
            }
        });

        switchBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return panel;
    }

    private void openDashboard(int userId) {
        ExpenseTrackerService service = new ExpenseTrackerService(userId);
        DashboardFrame dashboard = new DashboardFrame(service);
        dashboard.setVisible(true);
        this.dispose();
    }
}
