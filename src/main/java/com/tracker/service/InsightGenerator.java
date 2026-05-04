package com.tracker.service;

import com.tracker.model.Budget;
import com.tracker.model.Credit;
import com.tracker.model.Expense;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsightGenerator {

    public static List<String> generateInsights(List<Expense> expenses, Budget budget, Credit credit) {
        List<String> insights = new ArrayList<>();
        
        if (expenses.isEmpty()) {
            insights.add("<html><i>Start tracking expenses to get insights!</i></html>");
            return insights;
        }

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        // 0. Credit Alerts
        if (credit != null && credit.getTotalAmount() > 0) {
            if (totalExpenses > credit.getTotalAmount()) {
                insights.add(String.format("<html>🚨 <font color='red'><b>ALERT:</b></font> Your total expenses (₹%.2f) exceed your credited amount (₹%.2f)!</html>", totalExpenses, credit.getTotalAmount()));
            }
        }

        // 1. Budget Alerts
        if (budget != null && budget.getMonthlyLimit() > 0) {
            double budgetLimit = budget.getMonthlyLimit();
            double percentageUsed = (totalExpenses / budgetLimit) * 100;
            
            if (percentageUsed >= 100) {
                insights.add("<html>⚠️ <font color='red'>You have exceeded your monthly budget!</font></html>");
            } else if (percentageUsed >= 80) {
                insights.add(String.format("<html>⚠️ Warning: You have used %.1f%% of your budget.</html>", percentageUsed));
            } else {
                insights.add(String.format("<html>✅ You are within your budget (%.1f%% used).</html>", percentageUsed));
            }
        }

        // 2. Category Analysis
        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getMainCategory, Collectors.summingDouble(Expense::getAmount)));

        String topCategory = "";
        double maxCategoryTotal = 0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > maxCategoryTotal) {
                maxCategoryTotal = entry.getValue();
                topCategory = entry.getKey();
            }
        }

        if (!topCategory.isEmpty() && totalExpenses > 0) {
            double percentage = (maxCategoryTotal / totalExpenses) * 100;
            insights.add(String.format("<html>💡 <b>Highest Category:</b> You are spending most on <b>%s</b> (%.1f%% of total).</html>", topCategory, percentage));
        }

        // 3. Monthly Trend Analysis
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;

        double currentMonthTotal = 0;
        double prevMonthTotal = 0;

        for (Expense exp : expenses) {
            LocalDate expDate = exp.getDate().toLocalDate();
            if (expDate.getYear() == currentYear && expDate.getMonthValue() == currentMonth) {
                currentMonthTotal += exp.getAmount();
            } else if (expDate.getYear() == prevYear && expDate.getMonthValue() == prevMonth) {
                prevMonthTotal += exp.getAmount();
            }
        }

        if (prevMonthTotal > 0) {
            double difference = currentMonthTotal - prevMonthTotal;
            double percentageChange = (difference / prevMonthTotal) * 100;
            if (percentageChange > 0) {
                insights.add(String.format("<html>📈 <b>Trend:</b> Your expenses <font color='red'>increased</font> by %.1f%% this month compared to last month.</html>", percentageChange));
            } else if (percentageChange < 0) {
                insights.add(String.format("<html>📉 <b>Trend:</b> Your expenses <font color='green'>decreased</font> by %.1f%% this month compared to last month. Good job!</html>", Math.abs(percentageChange)));
            } else {
                insights.add("<html>➡️ <b>Trend:</b> Your spending this month is exactly the same as last month.</html>");
            }
        } else if (currentMonthTotal > 0) {
             insights.add("<html>📈 <b>Trend:</b> This is your first month of spending. Keep tracking!</html>");
        }

        return insights;
    }
}
