package dto;

import domain.ExpenseCategory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public ExpenseCategory getCategory() {
        return category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseDto)) return false;
        ExpenseDto that = (ExpenseDto) o;
        return Double.compare(that.amount, amount) == 0 && userId == that.userId && category == that.category && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, category, date, userId);
    }

    @Override
    public String toString() {
        return "ExpenseDto{" +
                "amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}
