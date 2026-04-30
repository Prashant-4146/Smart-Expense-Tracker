package com.tracker.service;

import com.tracker.model.Budget;
import com.tracker.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsightGenerator {

    public static List<String> generateInsights(List<Expense> expenses, Budget budget) {
        List<String> insights = new ArrayList<>();
        
        if (expenses.isEmpty()) {
            insights.add("Start tracking expenses to get insights!");
            return insights;
        }

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        // 1. Budget Alerts
        if (budget != null && budget.getMonthlyLimit() > 0) {
            double budgetLimit = budget.getMonthlyLimit();
            double percentageUsed = (totalExpenses / budgetLimit) * 100;
            
            if (percentageUsed >= 100) {
                insights.add("⚠️ You have exceeded your monthly budget!");
            } else if (percentageUsed >= 80) {
                insights.add(String.format("⚠️ Warning: You have used %.1f%% of your budget.", percentageUsed));
            } else {
                insights.add(String.format("✅ You are within your budget (%.1f%% used).", percentageUsed));
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
            insights.add(String.format("💡 You spent %.1f%% of your money on '%s'.", percentage, topCategory));
            
            if (topCategory.equals("Personal") && percentage > 50) {
                insights.add("🤖 Suggestion: Try reducing your personal spending.");
            }
        }

        // 3. Find top sub-category
        Map<String, Double> subCategoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getSubCategory, Collectors.summingDouble(Expense::getAmount)));
                
        if (subCategoryTotals.containsKey("Food / Restaurants")) {
            double foodTotal = subCategoryTotals.get("Food / Restaurants");
            if ((foodTotal / totalExpenses) > 0.3) {
                 insights.add("🍔 High spending on Restaurants ⚠️ Consider cooking at home.");
            }
        }

        return insights;
    }
}
