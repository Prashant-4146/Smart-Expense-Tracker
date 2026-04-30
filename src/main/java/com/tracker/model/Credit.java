package com.tracker.model;

public class Credit {
    private int id;
    private double totalAmount;
    private int userId;

    public Credit() {}

    public Credit(int id, double totalAmount, int userId) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
