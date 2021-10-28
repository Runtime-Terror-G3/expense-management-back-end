import java.util.Date;

public class MonthlyBudget implements Entity<Integer> {
    private Integer id;
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
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id = integer;
    }
}
