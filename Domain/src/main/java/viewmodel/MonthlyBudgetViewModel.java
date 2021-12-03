package viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import domain.MonthlyBudget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.List;

public class MonthlyBudgetViewModel implements Serializable {
    private int id;
    private double income;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date date;

    public MonthlyBudgetViewModel(int id, double income, Date date) {
        this.id = id;
        this.income = income;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    /**
     * transforms an MonthlyBudget object to and MonthlyBudgetViewModel object
     */
    public static MonthlyBudgetViewModel fromMonthlyBudget(MonthlyBudget monthlyBudget) {
        return new MonthlyBudgetViewModel(
                monthlyBudget.getId(),

                monthlyBudget.getIncome(),

                monthlyBudget.getDate()
        );
    }

    public static Iterable<MonthlyBudgetViewModel> fromMonthlyBudgetList(Iterable<MonthlyBudget> monthlyBudgetList) {
        List<MonthlyBudgetViewModel> monthlyBudgetViewModelList = new ArrayList<>();

        for (MonthlyBudget monthlyBudget : monthlyBudgetList) {
            monthlyBudgetViewModelList.add(fromMonthlyBudget(monthlyBudget));
        }

        return monthlyBudgetViewModelList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyBudgetViewModel that = (MonthlyBudgetViewModel) o;
        return id == that.id &&
                Double.compare(that.income, income) == 0 &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, income, date);
    }

    @Override
    public String toString() {
        return "MonthlyBudgetViewModel{" +
                "id=" + id +
                ", income=" + income +
                ", date=" + date +
                '}';
    }

}

