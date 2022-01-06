package viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import domain.Expense;
import domain.ExpenseCategory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ExpenseViewModel implements Serializable {
    private final int id;
    private final double amount;
    private final ExpenseCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime date;

    public ExpenseViewModel(int id, double amount, ExpenseCategory category, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
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

    /**
     * transforms an Expense object to and ExpenseViewModel object
     * @param expense the expens to transform
     * @return the ExpenseViewModel for the given expense
     */
    public static ExpenseViewModel fromExpense(Expense expense) {
        return new ExpenseViewModel(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseViewModel)) return false;
        ExpenseViewModel that = (ExpenseViewModel) o;
        return id == that.id && Double.compare(that.amount, amount) == 0 && category == that.category && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, category, date);
    }

    @Override
    public String toString() {
        return "ExpenseViewModel{" +
                "id=" + id +
                ", amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                '}';
    }
}
