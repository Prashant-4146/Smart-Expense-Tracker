package com.tracker.ui;

import com.tracker.model.Budget;
import com.tracker.model.Credit;
import com.tracker.model.Expense;
import com.tracker.service.ExpenseTrackerService;
import com.tracker.ui.custom.ModernButton;
import com.tracker.ui.custom.ModernScrollBarUI;
import com.tracker.ui.custom.RoundedPanel;
import com.tracker.ui.custom.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private ExpenseTrackerService service;

    private JLabel totalCreditsLabel;
    private JLabel totalExpensesLabel;
    private JLabel remainingBalanceLabel;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private ReportPanel reportPanel;
    
    // Filters and Insights
    private JComboBox<String> filterCombo;
    private JComboBox<String> sortCombo;
    private JProgressBar budgetProgressBar;
    private JPanel insightsListPanel;

    public DashboardFrame(ExpenseTrackerService service) {
        this.service = service;
        setTitle("Smart Personal Finance Assistant");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_COLOR);
        setLayout(new BorderLayout(20, 20));
        
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        refreshData();
    }

    private void initUI() {
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setOpaque(false);

        // --- Top Panel: Summary Cards ---
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setOpaque(false);

        totalCreditsLabel = createSummaryCard("Total Credits", "💳", summaryPanel, Theme.PRIMARY);
        totalExpensesLabel = createSummaryCard("Total Expenses", "📉", summaryPanel, Theme.DANGER);
        remainingBalanceLabel = createSummaryCard("Remaining Balance", "💰", summaryPanel, Theme.SUCCESS);
        
        mainContentPanel.add(summaryPanel, BorderLayout.NORTH);

        // --- Center Area: Split left (Table & Filters) and right (Charts & Insights) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        centerPanel.add(createTablePanel());

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        rightPanel.setOpaque(false);
        
        reportPanel = new ReportPanel(service);
        rightPanel.add(reportPanel);
        rightPanel.add(createInsightsPanel());

        centerPanel.add(rightPanel);

        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JLabel createSummaryCard(String title, String iconStr, JPanel parent, Color valueColor) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titleBox = new JPanel(new BorderLayout());
        titleBox.setOpaque(false);
        
        JLabel iconLabel = new JLabel(iconStr);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(Theme.TEXT_SECONDARY);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        titleBox.add(titleLabel, BorderLayout.WEST);
        titleBox.add(iconLabel, BorderLayout.EAST);

        JLabel valueLabel = new JLabel("₹0.00");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(valueColor);

        card.add(titleBox, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        parent.add(card);
        return valueLabel;
    }

    private JPanel createTablePanel() {
        RoundedPanel tableContainer = new RoundedPanel(20);
        tableContainer.setLayout(new BorderLayout(0, 15));
        tableContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Filters and Title
        JPanel topBox = new JPanel(new BorderLayout());
        topBox.setOpaque(false);
        
        JLabel tableTitle = new JLabel("Recent Expenses");
        tableTitle.setFont(Theme.FONT_HEADING);
        tableTitle.setForeground(Theme.TEXT_PRIMARY);
        topBox.add(tableTitle, BorderLayout.WEST);
        
        JPanel filterBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterBox.setOpaque(false);
        
        filterCombo = new JComboBox<>(new String[]{"All Categories", "Personal", "Official", "Others"});
        sortCombo = new JComboBox<>(new String[]{"Latest First", "Highest Amount", "Lowest Amount"});
        
        filterCombo.addActionListener(e -> refreshData());
        sortCombo.addActionListener(e -> refreshData());
        
        filterBox.add(new JLabel("Filter:"));
        filterBox.add(filterCombo);
        filterBox.add(new JLabel("Sort:"));
        filterBox.add(sortCombo);
        topBox.add(filterBox, BorderLayout.EAST);
        
        tableContainer.add(topBox, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Main Category", "Sub Category", "Amount", "Desc"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(tableModel);
        
        expenseTable.setBackground(Theme.CARD_BG);
        expenseTable.setForeground(Theme.TEXT_PRIMARY);
        expenseTable.setFont(Theme.FONT_REGULAR);
        expenseTable.setRowHeight(40);
        expenseTable.setShowGrid(false);
        expenseTable.setIntercellSpacing(new Dimension(0, 0));
        expenseTable.setSelectionBackground(Theme.BORDER_COLOR);
        expenseTable.setSelectionForeground(Theme.TEXT_PRIMARY);
        expenseTable.setFocusable(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Theme.CARD_BG : Theme.BG_COLOR);
                    
                    // Color code Main Category column
                    if (column == 2) {
                        String cat = (String) value;
                        if (cat.contains("Personal")) c.setForeground(Theme.PRIMARY);
                        else if (cat.contains("Official")) c.setForeground(new Color(155, 89, 182)); // Purple
                        else c.setForeground(Theme.TEXT_SECONDARY);
                    } else {
                        c.setForeground(Theme.TEXT_PRIMARY);
                    }
                }
                return c;
            }
        };
        for (int i = 0; i < expenseTable.getColumnCount(); i++) {
            expenseTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JTableHeader header = expenseTable.getTableHeader();
        header.setBackground(Theme.BG_COLOR);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setFont(Theme.FONT_SMALL);
        header.setBorder(BorderFactory.createEmptyBorder());
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Theme.CARD_BG);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        tableContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        ModernButton updateCreditBtn = new ModernButton("Update Credits", Theme.BORDER_COLOR);
        ModernButton addBtn = new ModernButton("Add Expense", Theme.PRIMARY);
        ModernButton deleteBtn = new ModernButton("Delete", Theme.DANGER);

        updateCreditBtn.addActionListener(e -> showUpdateCreditDialog());
        addBtn.addActionListener(e -> showAddExpenseDialog());
        deleteBtn.addActionListener(e -> deleteSelectedExpense());

        actionPanel.add(updateCreditBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(addBtn);

        tableContainer.add(actionPanel, BorderLayout.SOUTH);

        return tableContainer;
    }

    private JPanel createInsightsPanel() {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Smart Insights & Budget");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);
        
        ModernButton setBudgetBtn = new ModernButton("Set Budget", Theme.BORDER_COLOR);
        setBudgetBtn.setPreferredSize(new Dimension(100, 30));
        setBudgetBtn.setFont(Theme.FONT_SMALL);
        setBudgetBtn.addActionListener(e -> showSetBudgetDialog());
        header.add(setBudgetBtn, BorderLayout.EAST);
        
        panel.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);

        // Budget Progress
        JPanel budgetPanel = new JPanel(new BorderLayout(5, 5));
        budgetPanel.setOpaque(false);
        JLabel budgetLabel = new JLabel("Monthly Budget Progress:");
        budgetLabel.setForeground(Theme.TEXT_SECONDARY);
        budgetPanel.add(budgetLabel, BorderLayout.NORTH);
        
        budgetProgressBar = new JProgressBar(0, 100);
        budgetProgressBar.setStringPainted(true);
        budgetProgressBar.setBackground(Theme.BG_COLOR);
        budgetProgressBar.setForeground(Theme.SUCCESS);
        budgetProgressBar.setBorderPainted(false);
        budgetPanel.add(budgetProgressBar, BorderLayout.CENTER);
        
        contentPanel.add(budgetPanel, BorderLayout.NORTH);

        // Insights List
        insightsListPanel = new JPanel();
        insightsListPanel.setLayout(new BoxLayout(insightsListPanel, BoxLayout.Y_AXIS));
        insightsListPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(insightsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Theme.CARD_BG);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private void showSetBudgetDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Monthly Budget Limit (₹):", "Set Budget", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double limit = Double.parseDouble(input);
                if (limit >= 0) {
                    service.updateBudget(limit);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Budget cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddExpenseDialog() {
        AddExpenseDialog dialog = new AddExpenseDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Expense newExpense = dialog.getExpense();
            service.addExpense(newExpense);
            refreshData();
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow >= 0) {
            int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this expense?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteExpense(expenseId);
                refreshData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
        }
    }

    private void showUpdateCreditDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Total Credits (₹):", "Update Credits", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double newCredit = Double.parseDouble(input);
                if (newCredit >= 0) {
                    service.updateCredit(newCredit);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Credits cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        // Update summary cards
        Credit credit = service.getCredit();
        double totalCredits = credit != null ? credit.getTotalAmount() : 0.0;
        double totalExpenses = service.getTotalExpenses();
        double remainingBalance = service.getRemainingBalance();

        totalCreditsLabel.setText(String.format("₹%.2f", totalCredits));
        totalExpensesLabel.setText(String.format("₹%.2f", totalExpenses));
        remainingBalanceLabel.setText(String.format("₹%.2f", remainingBalance));

        if (remainingBalance < 0) {
            remainingBalanceLabel.setForeground(Theme.DANGER);
        } else {
            remainingBalanceLabel.setForeground(Theme.SUCCESS);
        }

        // Fetch and Filter/Sort Table Data
        List<Expense> expenses;
        String filter = (String) filterCombo.getSelectedItem();
        if ("All Categories".equals(filter)) {
            expenses = service.getAllExpenses();
        } else {
            expenses = service.getExpensesByMainCategory(filter);
        }
        
        String sort = (String) sortCombo.getSelectedItem();
        if ("Highest Amount".equals(sort)) {
            expenses.sort((e1, e2) -> Double.compare(e2.getAmount(), e1.getAmount()));
        } else if ("Lowest Amount".equals(sort)) {
            expenses.sort((e1, e2) -> Double.compare(e1.getAmount(), e2.getAmount()));
        }

        tableModel.setRowCount(0);
        for (Expense exp : expenses) {
            String icon = "❔";
            if (exp.getMainCategory().equals("Personal")) icon = "👕 ";
            else if (exp.getMainCategory().equals("Official")) icon = "🎓 ";
            else if (exp.getMainCategory().equals("Others")) icon = "📁 ";

            tableModel.addRow(new Object[]{
                    exp.getId(),
                    exp.getDate(),
                    icon + exp.getMainCategory(),
                    exp.getSubCategory(),
                    "₹" + String.format("%.2f", exp.getAmount()),
                    exp.getDescription()
            });
        }

        // Update Insights & Budget
        Budget budget = service.getBudget();
        if (budget != null && budget.getMonthlyLimit() > 0) {
            int progress = (int) ((totalExpenses / budget.getMonthlyLimit()) * 100);
            budgetProgressBar.setValue(Math.min(progress, 100));
            budgetProgressBar.setString(String.format("%d%% (₹%.2f / ₹%.2f)", progress, totalExpenses, budget.getMonthlyLimit()));
            
            if (progress >= 100) budgetProgressBar.setForeground(Theme.DANGER);
            else if (progress >= 80) budgetProgressBar.setForeground(new Color(243, 156, 18)); // Orange
            else budgetProgressBar.setForeground(Theme.SUCCESS);
        } else {
            budgetProgressBar.setValue(0);
            budgetProgressBar.setString("Budget not set");
            budgetProgressBar.setForeground(Theme.SUCCESS);
        }

        insightsListPanel.removeAll();
        List<String> insights = service.getInsights();
        for (String insight : insights) {
            JLabel lbl = new JLabel("<html><p style='width:300px;'>" + insight + "</p></html>");
            lbl.setForeground(Theme.TEXT_SECONDARY);
            lbl.setFont(Theme.FONT_REGULAR);
            lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
            insightsListPanel.add(lbl);
        }
        insightsListPanel.revalidate();
        insightsListPanel.repaint();

        // Update charts
        if (reportPanel != null) {
            reportPanel.refreshCharts();
        }
    }
}
