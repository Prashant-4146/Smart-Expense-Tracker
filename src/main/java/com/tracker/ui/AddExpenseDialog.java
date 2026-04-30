package com.tracker.ui;

import com.tracker.model.Expense;
import com.tracker.service.SmartCategorizer;
import com.tracker.ui.custom.ModernButton;
import com.tracker.ui.custom.RoundedPanel;
import com.tracker.ui.custom.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class AddExpenseDialog extends JDialog {
    private JTextField amountField;
    private JComboBox<String> mainCategoryCombo;
    private JComboBox<String> subCategoryCombo;
    private JTextField dateField;
    private JTextField descriptionField;
    private boolean isConfirmed = false;
    private Expense expense;

    public AddExpenseDialog(JFrame parent) {
        super(parent, "Add Expense", true);
        setLayout(new BorderLayout());
        setSize(400, 350);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_COLOR);

        RoundedPanel formPanel = new RoundedPanel(20);
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Amount
        JLabel amountLabel = new JLabel("Amount (₹):");
        amountLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(amountLabel);
        amountField = new JTextField();
        formPanel.add(amountField);

        // Description with auto-suggest listener
        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(descLabel);
        descriptionField = new JTextField();
        formPanel.add(descriptionField);

        // Main Category
        JLabel mainCatLabel = new JLabel("Main Category:");
        mainCatLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(mainCatLabel);
        String[] mainCategories = {"Personal", "Official", "Others"};
        mainCategoryCombo = new JComboBox<>(mainCategories);
        formPanel.add(mainCategoryCombo);

        // Sub Category
        JLabel subCatLabel = new JLabel("Sub Category:");
        subCatLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(subCatLabel);
        subCategoryCombo = new JComboBox<>();
        updateSubCategories();
        formPanel.add(subCategoryCombo);

        mainCategoryCombo.addActionListener(e -> updateSubCategories());

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(dateLabel);
        dateField = new JTextField(LocalDate.now().toString());
        formPanel.add(dateField);

        // Add auto-suggest listener to description
        descriptionField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { suggest(); }
            public void removeUpdate(DocumentEvent e) { suggest(); }
            public void insertUpdate(DocumentEvent e) { suggest(); }

            private void suggest() {
                String desc = descriptionField.getText();
                String[] suggested = SmartCategorizer.suggestCategory(desc);
                if (suggested.length == 2) {
                    mainCategoryCombo.setSelectedItem(suggested[0]);
                    subCategoryCombo.setSelectedItem(suggested[1]);
                }
            }
        });

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        ModernButton saveButton = new ModernButton("Save", Theme.SUCCESS);
        ModernButton cancelButton = new ModernButton("Cancel", Theme.BORDER_COLOR);

        saveButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                isConfirmed = true;
                expense = new Expense();
                expense.setAmount(Double.parseDouble(amountField.getText().trim()));
                expense.setMainCategory((String) mainCategoryCombo.getSelectedItem());
                expense.setSubCategory((String) subCategoryCombo.getSelectedItem());
                expense.setDate(Date.valueOf(dateField.getText().trim()));
                expense.setDescription(descriptionField.getText().trim());
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateSubCategories() {
        String main = (String) mainCategoryCombo.getSelectedItem();
        subCategoryCombo.removeAllItems();
        if ("Personal".equals(main)) {
            subCategoryCombo.addItem("Clothes");
            subCategoryCombo.addItem("Food / Restaurants");
            subCategoryCombo.addItem("Travel");
            subCategoryCombo.addItem("Entertainment");
        } else if ("Official".equals(main)) {
            subCategoryCombo.addItem("Books");
            subCategoryCombo.addItem("Printing");
            subCategoryCombo.addItem("Projects");
            subCategoryCombo.addItem("Fees");
            subCategoryCombo.addItem("Supplies");
        } else {
            subCategoryCombo.addItem("Miscellaneous");
        }
    }

    private boolean validateInput() {
        try {
            double amt = Double.parseDouble(amountField.getText().trim());
            if (amt < 0) {
                JOptionPane.showMessageDialog(this, "Amount cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            Date.valueOf(dateField.getText().trim());
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Expense getExpense() {
        return expense;
    }
}
