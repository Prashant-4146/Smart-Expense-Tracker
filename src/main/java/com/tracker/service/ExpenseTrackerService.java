package com.tracker.service;

import com.tracker.dao.BudgetDAO;
import com.tracker.dao.CreditDAO;
import com.tracker.dao.ExpenseDAO;
import com.tracker.model.Budget;
import com.tracker.model.Credit;
import com.tracker.model.Expense;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseTrackerService {
    private ExpenseDAO expenseDAO;
    private CreditDAO creditDAO;
    private BudgetDAO budgetDAO;
    private int currentUserId;

    public ExpenseTrackerService(int currentUserId) {
        this.expenseDAO = new ExpenseDAO();
        this.creditDAO = new CreditDAO();
        this.budgetDAO = new BudgetDAO();
        this.currentUserId = currentUserId;
    }

    // --- Expense Management ---
    
    public void addExpense(Expense expense) {
        expense.setUserId(currentUserId);
        expenseDAO.addExpense(expense);
    }

    public void updateExpense(Expense expense) {
        expense.setUserId(currentUserId);
        expenseDAO.updateExpense(expense);
    }

    public void deleteExpense(int expenseId) {
        expenseDAO.deleteExpense(expenseId, currentUserId);
    }

    public List<Expense> getAllExpenses() {
        return expenseDAO.getExpensesByUserId(currentUserId);
    }
    
    // Sort expenses
    public List<Expense> getExpensesSortedByAmount(boolean descending) {
        List<Expense> expenses = getAllExpenses();
        if (descending) {
            expenses.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        } else {
            expenses.sort(Comparator.comparingDouble(Expense::getAmount));
        }
        return expenses;
    }
    
    // Filter expenses
    public List<Expense> getExpensesByMainCategory(String mainCategory) {
        return getAllExpenses().stream()
                .filter(e -> e.getMainCategory().equalsIgnoreCase(mainCategory))
                .collect(Collectors.toList());
    }

    public double getTotalExpenses() {
        return getAllExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // --- Credit Management ---

    public Credit getCredit() {
        return creditDAO.getCreditByUserId(currentUserId);
    }

    public void updateCredit(double newTotalAmount) {
        Credit credit = getCredit();
        if (credit == null) {
            credit = new Credit(0, newTotalAmount, currentUserId);
            creditDAO.insertCredit(credit);
        } else {
            credit.setTotalAmount(newTotalAmount);
            creditDAO.updateCredit(credit);
        }
    }

    public double getRemainingBalance() {
        Credit credit = getCredit();
        double totalCredit = (credit != null) ? credit.getTotalAmount() : 0.0;
        return totalCredit - getTotalExpenses();
    }

    // --- Budget Management ---
    
    public Budget getBudget() {
        return budgetDAO.getBudgetByUserId(currentUserId);
    }
    
    public void updateBudget(double limit) {
        budgetDAO.upsertBudget(new Budget(0, limit, currentUserId));
    }
    
    // --- Insights & Analytics ---

    public Map<String, Double> getExpensesByCategory() {
        return getAllExpenses().stream()
                .collect(Collectors.groupingBy(
                        Expense::getMainCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }
    
    public List<String> getInsights() {
        return InsightGenerator.generateInsights(getAllExpenses(), getBudget());
    }
}
