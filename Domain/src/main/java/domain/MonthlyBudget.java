package domain;

import java.util.Date;
import java.util.Objects;

public class MonthlyBudget implements Entity<Integer> {
    private int id;
    private User user;
    private double income;
    private Date date;

    public MonthlyBudget(User user, double income, Date date) {
        this.user = user;
        this.income = income;
        this.date = date;
    }

    public MonthlyBudget() {
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer integer) {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return "MonthlyBudget{" +
                "id=" + id +
                ", user=" + user +
                ", income=" + income +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyBudget that = (MonthlyBudget) o;
        return id == that.id && Double.compare(that.income, income) == 0 && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, income);
    }
}
