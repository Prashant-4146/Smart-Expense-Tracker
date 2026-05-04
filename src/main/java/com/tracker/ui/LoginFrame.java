package com.tracker.ui;

import com.tracker.dao.CreditDAO;
import com.tracker.dao.UserDAO;
import com.tracker.model.Credit;
import com.tracker.model.User;
import com.tracker.service.ExpenseTrackerService;
import com.tracker.ui.custom.ModernButton;
import com.tracker.ui.custom.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel rightPanel;

    private UserDAO userDAO;
    private CreditDAO creditDAO;

    public LoginFrame() {
        setTitle("SmartTracker");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_COLOR);

        userDAO = new UserDAO();
        creditDAO = new CreditDAO();

        // Left Branding Panel
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(0, 122, 255); // Theme.PRIMARY
                Color color2 = new Color(138, 43, 226); // Purpleish
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        leftPanel.setPreferredSize(new Dimension(400, 550));
        leftPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 10, 10, 10);

        JLabel logoLabel = new JLabel("✨");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        logoLabel.setForeground(Color.WHITE);
        leftPanel.add(logoLabel, gbcLeft);

        gbcLeft.gridy = 1;
        JLabel titleLabel = new JLabel("SmartTracker");
        // Using a fancy cursive-like or bold stylized font
        titleLabel.setFont(new Font("Monotype Corsiva", Font.BOLD, 48));
        // Fallback font if Monotype Corsiva is not available
        if (!titleLabel.getFont().getFamily().equals("Monotype Corsiva")) {
            titleLabel.setFont(new Font("Brush Script MT", Font.BOLD, 48));
        }
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel, gbcLeft);

        gbcLeft.gridy = 2;
        JLabel subtitleLabel = new JLabel("Master Your Finances");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        leftPanel.add(subtitleLabel, gbcLeft);

        add(leftPanel, BorderLayout.WEST);

        // Right Forms Panel
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(Theme.BG_COLOR);

        rightPanel.add(createLoginPanel(), "Login");
        rightPanel.add(createSignUpPanel(), "SignUp");

        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 30, 20);
        panel.add(titleLabel, gbc);

        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Theme.TEXT_SECONDARY);
        emailLabel.setFont(Theme.FONT_HEADING);
        panel.add(emailLabel, gbc);

        gbc.gridy = 2;
        JTextField emailField = createStyledTextField();
        panel.add(emailField, gbc);

        gbc.gridy = 3;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Theme.TEXT_SECONDARY);
        passLabel.setFont(Theme.FONT_HEADING);
        panel.add(passLabel, gbc);

        gbc.gridy = 4;
        JPasswordField passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);

        ModernButton loginBtn = new ModernButton("Login", Theme.PRIMARY);
        loginBtn.setPreferredSize(new Dimension(200, 45));
        
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 20, 10, 20);
        panel.add(loginBtn, gbc);

        JLabel switchLabel = new JLabel("<html>Don't have an account? <font color='#007AFF'>Sign Up</font></html>");
        switchLabel.setFont(Theme.FONT_REGULAR);
        switchLabel.setForeground(Theme.TEXT_SECONDARY);
        switchLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel.add(switchLabel, gbc);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = userDAO.authenticateUser(email, password);
            if (user != null) {
                openDashboard(user.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        switchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "SignUp");
            }
        });

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 20, 20);
        panel.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridwidth = 2;
        
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Theme.TEXT_SECONDARY);
        nameLabel.setFont(Theme.FONT_HEADING);
        panel.add(nameLabel, gbc);

        gbc.gridy = 2;
        JTextField nameField = createStyledTextField();
        panel.add(nameField, gbc);

        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Theme.TEXT_SECONDARY);
        emailLabel.setFont(Theme.FONT_HEADING);
        panel.add(emailLabel, gbc);

        gbc.gridy = 4;
        JTextField emailField = createStyledTextField();
        panel.add(emailField, gbc);

        gbc.gridy = 5;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Theme.TEXT_SECONDARY);
        passLabel.setFont(Theme.FONT_HEADING);
        panel.add(passLabel, gbc);

        gbc.gridy = 6;
        JPasswordField passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);

        ModernButton signUpBtn = new ModernButton("Sign Up", Theme.SUCCESS);
        signUpBtn.setPreferredSize(new Dimension(200, 45));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(25, 20, 10, 20);
        panel.add(signUpBtn, gbc);

        JLabel switchLabel = new JLabel("<html>Already have an account? <font color='#007AFF'>Login</font></html>");
        switchLabel.setFont(Theme.FONT_REGULAR);
        switchLabel.setForeground(Theme.TEXT_SECONDARY);
        switchLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel.add(switchLabel, gbc);

        signUpBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = new User(0, name, email, password);
            int userId = userDAO.registerUser(user);
            if (userId > 0) {
                // Insert initial credit for the new user
                creditDAO.insertCredit(new Credit(0, 0.0, userId));
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                openDashboard(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. This is likely due to a database connection issue or the email already existing.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        switchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "Login");
            }
        });

        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(Theme.CARD_BG);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.TEXT_PRIMARY);
        field.setFont(Theme.FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(Theme.CARD_BG);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.TEXT_PRIMARY);
        field.setFont(Theme.FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void openDashboard(int userId) {
        ExpenseTrackerService service = new ExpenseTrackerService(userId);
        DashboardFrame dashboard = new DashboardFrame(service);
        dashboard.setVisible(true);
        this.dispose();
    }
}
