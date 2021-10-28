import java.time.LocalDateTime;

public class Expense implements Entity<Integer> {
    private Integer id;
    private double amount;
    private ExpenseCategory category;
    private LocalDateTime date;
    private User user;

    public Expense(double amount, ExpenseCategory category, LocalDateTime date, User user) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.user = user;
    }

    public Expense() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
