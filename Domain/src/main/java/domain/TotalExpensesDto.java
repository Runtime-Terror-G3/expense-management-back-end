package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TotalExpensesDto implements Serializable {
    private double amount;
    private LocalDate dateTime;

    public TotalExpensesDto(double amount, LocalDateTime date) {
        this.amount = amount;
        this.dateTime = date.toLocalDate();
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "TotalExpensesDto{" +
                "amount=" + amount +
                ", dateTime=" + dateTime +
                '}';
    }
}
