package dto;

import domain.MonthlyBudget;
import domain.User;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MonthlyBudgetDto implements Serializable {
    private int userId;
    private double income;
    private Date date;

    public MonthlyBudgetDto(int userId, double income, Date date) {
        this.userId = userId;
        this.income = income;
        this.date = date;
    }

    public MonthlyBudgetDto() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MonthlyBudgetDto{" +
                ", userId=" + userId +
                ", income=" + income +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyBudgetDto that = (MonthlyBudgetDto) o;
        return userId == that.userId &&
                Double.compare(that.income, income) == 0 &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, income, date);
    }
}
