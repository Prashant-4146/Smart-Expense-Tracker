package com.tracker.model;

public class Budget {
    private int id;
    private double monthlyLimit;
    private int userId;

    public Budget() {}

    public Budget(int id, double monthlyLimit, int userId) {
        this.id = id;
        this.monthlyLimit = monthlyLimit;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(double monthlyLimit) { this.monthlyLimit = monthlyLimit; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
