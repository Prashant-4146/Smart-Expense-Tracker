package com.tracker.model;

import java.sql.Date;

public class Expense {
    private int id;
    private double amount;
    private String mainCategory;
    private String subCategory;
    private Date date;
    private String description;
    private int userId;

    public Expense() {}

    public Expense(int id, double amount, String mainCategory, String subCategory, Date date, String description, int userId) {
        this.id = id;
        this.amount = amount;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.date = date;
        this.description = description;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMainCategory() { return mainCategory; }
    public void setMainCategory(String mainCategory) { this.mainCategory = mainCategory; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String getCategory() { return mainCategory + " - " + subCategory; } // legacy helper

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
