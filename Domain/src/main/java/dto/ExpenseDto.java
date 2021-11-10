package dto;

import domain.ExpenseCategory;
import java.io.Serializable;
import java.time.LocalDateTime;

public class ExpenseDto implements Serializable {
    private double amount;
    private ExpenseCategory category;
    private LocalDateTime date;
    private int userId;

    public ExpenseDto() {
    }

    public double getAmount() {
        return amount;
    }

    public ExpenseDto setAmount(double amount) {
        this.amount = amount;

        return this;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public ExpenseDto setCategory(ExpenseCategory category) {
        this.category = category;

        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ExpenseDto setDate(LocalDateTime date) {
        this.date = date;

        return this;
    }

    public int getUserId() {
        return userId;
    }

    public ExpenseDto setUserId(int userId) {
        this.userId = userId;

        return this;
    }
}
