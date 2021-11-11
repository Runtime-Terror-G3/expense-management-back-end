package viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import domain.ExpenseCategory;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExpenseViewModel implements Serializable {
    private int id;
    private double amount;
    private ExpenseCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    public ExpenseViewModel(int id, double amount, ExpenseCategory category, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public ExpenseViewModel setId(int id) {
        this.id = id;

        return this;
    }

    public double getAmount() {
        return amount;
    }

    public ExpenseViewModel setAmount(double amount) {
        this.amount = amount;

        return this;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public ExpenseViewModel setCategory(ExpenseCategory category) {
        this.category = category;

        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ExpenseViewModel setDate(LocalDateTime date) {
        this.date = date;

        return this;
    }
}
