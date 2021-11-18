package domain;

import dto.ExpenseDto;
import viewmodel.ExpenseViewModel;

import java.time.LocalDateTime;
import java.util.Objects;

public class Expense implements Entity<Integer> {
    private int id;
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

    public Expense(int id, double amount, ExpenseCategory category, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Expense() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
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

    /**
     * transforms an ExpenseDto object to an Expense object
     */
    public static Expense fromExpenseDto(ExpenseDto expenseDto) {
        return new Expense(
                expenseDto.getAmount(),
                expenseDto.getCategory(),
                expenseDto.getDate(),
                new User(expenseDto.getUserId())
        );
    }

    public  ExpenseViewModel toExpenseViewModel(){
        return new ExpenseViewModel(this.id,this.amount,this.category,this.date);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id == expense.id && Double.compare(expense.amount, amount) == 0 && category == expense.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, category);
    }
}
