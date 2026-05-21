package isi.shoppingCart.entities;

import java.time.LocalDateTime;
public class Payment {
    private int id;
    private double amount;
    private boolean approved;
    private String reference;
    private LocalDateTime timestamp;

    public Payment(int id, double amount, boolean approved, String reference) {
        this.id = id;
        this.amount = amount;
        this.approved = approved;
        this.reference = reference;
        this.timestamp = LocalDateTime.now();

    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getReference() {
        return reference;
    }
}

