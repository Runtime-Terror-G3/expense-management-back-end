import java.util.Date;

public class MonthlyBudget extends Entity<Integer> {
    private User user;
    private double income;
    private Date date;

    public MonthlyBudget(User user, double income, Date date) {
        this.user = user;
        this.income = income;
        this.date = date;
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
}
