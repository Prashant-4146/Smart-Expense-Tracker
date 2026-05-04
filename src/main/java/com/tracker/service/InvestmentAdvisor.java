package com.tracker.service;

import java.util.ArrayList;
import java.util.List;

public class InvestmentAdvisor {

    public static List<String> getStockSuggestions(double remainingBalance) {
        List<String> suggestions = new ArrayList<>();
        if (remainingBalance < 500) {
            suggestions.add("<html><i>Balance too low for major stock investments. Focus on saving first.</i></html>");
            return suggestions;
        }

        // Static Real-World Stock Data (India)
        // Name, Price, Expected Return, Risk Level
        Object[][] stocks = {
            {"Reliance Industries", 2900.0, "12-15%", "Medium"},
            {"TCS", 3800.0, "10-14%", "Low"},
            {"Infosys", 1600.0, "10-15%", "Medium"},
            {"HDFC Bank", 1450.0, "15-18%", "Low"},
            {"ICICI Bank", 1050.0, "16-20%", "Medium"},
            {"Tata Motors", 950.0, "20-25%", "High"}
        };

        double suggestedInvestmentTotal = remainingBalance * 0.4; // 40% of remaining balance for stocks

        for (Object[] stock : stocks) {
            String name = (String) stock[0];
            double price = (Double) stock[1];
            String returnRate = (String) stock[2];
            String risk = (String) stock[3];

            if (suggestedInvestmentTotal >= price) {
                int shares = (int) (suggestedInvestmentTotal / price);
                if (shares > 0 && suggestions.size() < 3) {
                    double investment = shares * price;
                    suggestions.add(String.format("<html><b>Stock: %s</b><br/>" +
                            "Price: ₹%.2f<br/>" +
                            "Expected Return: %s yearly<br/>" +
                            "Risk Level: %s<br/>" +
                            "<font color='#007AFF'>Suggested Investment: ₹%.2f (%d shares)</font></html>",
                            name, price, returnRate, risk, investment, shares));
                }
            }
        }
        
        if (suggestions.isEmpty() && remainingBalance >= 500) {
            suggestions.add("<html><i>Consider accumulating more balance to buy premium stocks.</i></html>");
        }

        return suggestions;
    }

    public static List<String> getSIPSuggestions(double remainingBalance) {
        List<String> suggestions = new ArrayList<>();
        double monthlySavings = remainingBalance * 0.3; // Allocate 30% of remaining for SIP

        if (monthlySavings < 500) {
            suggestions.add("<html><i>Minimum ₹500/month recommended for SIPs. Keep saving!</i></html>");
            return suggestions;
        }

        // Static Real-World SIP Data
        // Fund Name, Expected Annual Return (decimal)
        Object[][] sips = {
            {"SBI Bluechip Fund", 0.12},
            {"HDFC Top 100 Fund", 0.14},
            {"Axis Bluechip Fund", 0.13},
            {"Quant Small Cap Fund", 0.18}
        };

        int[] periods = {1, 3, 5}; // Years

        for (int i = 0; i < sips.length && suggestions.size() < 3; i++) {
            String name = (String) sips[i][0];
            double returnRate = (Double) sips[i][1];
            
            // Choose a random period for variety based on index
            int periodYears = periods[i % periods.length];
            int months = periodYears * 12;
            
            // Monthly Investment (round to nearest 500)
            double monthlyInv = Math.max(500, Math.floor(monthlySavings / 500) * 500);

            // Future Value Formula: P * [ (1+i)^n - 1 ] / i * (1+i)
            double monthlyRate = returnRate / 12;
            double futureValue = monthlyInv * (Math.pow(1 + monthlyRate, months) - 1) / monthlyRate * (1 + monthlyRate);

            suggestions.add(String.format("<html><b>SIP: %s</b><br/>" +
                    "Monthly Investment: ₹%.2f<br/>" +
                    "Expected Return: %d%% annually<br/>" +
                    "Investment Period: %d years<br/>" +
                    "<font color='#28a745'>Estimated Future Value: ₹%.2f</font></html>",
                    name, monthlyInv, (int)(returnRate * 100), periodYears, futureValue));
        }

        return suggestions;
    }
}
