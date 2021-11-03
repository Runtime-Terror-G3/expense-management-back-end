package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import domain.MonthlyBudget;
import domain.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Constants {
    public static final List<User> defaultUsers = List.of(
            new User("a@g.com", "A", "B", new Date(), "hash"),
            new User("b@g.com", "C", "D", new Date(new Date().getTime() - 3600000), "hash1"),
            new User("c@g.com", "E", "F", new Date(new Date().getTime() + 3600000), "hash2")
    );

    public static final User userToSave = new User("d@g.com", "G", "H", new Date(), "hash4");

    public static final List<Expense> defaultExpenses = List.of(
            new Expense(10, ExpenseCategory.Clothing, LocalDateTime.now(), defaultUsers.get(0)),
            new Expense(90, ExpenseCategory.Health, LocalDateTime.now(), defaultUsers.get(1)),
            new Expense(729, ExpenseCategory.Education, LocalDateTime.now(), defaultUsers.get(0)),
            new Expense(901, ExpenseCategory.Other, LocalDateTime.now(), defaultUsers.get(0))
    );

    public static final Expense expenseToSave = new Expense(123, ExpenseCategory.Housekeeping, LocalDateTime.now(), defaultUsers.get(2));

    public static final List<MonthlyBudget> defaultMonthlyBudgets = List.of(
            new MonthlyBudget(defaultUsers.get(0), 120, new Date()),
            new MonthlyBudget(defaultUsers.get(1), 900, new Date()),
            new MonthlyBudget(defaultUsers.get(0), 150, java.sql.Date.valueOf(LocalDateTime.now().minusMonths(2).toLocalDate()))
    );

    public static final int ID_MONTHLY_BUDGET = 1;

    public static final MonthlyBudget budgetToSave = new MonthlyBudget(defaultUsers.get(0), 777, new Date());
}
