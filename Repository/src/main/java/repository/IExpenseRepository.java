package repository;

import domain.Expense;
import domain.ExpenseCategory;

import java.util.Optional;

public interface IExpenseRepository extends IRepository<Integer, Expense> {
    /**
     * Get the expenses for a user filtered by a date interval
     * @param userId id of the user the expenses belong to
     * @param expenseCategory category of the expenses
     * @param startDate start date of the interval
     * @param endDate end date of the interval
     * @return an optional containing a list with the requested expenses
     */
    Optional<Iterable<Expense>> findByFilter(int userId, String category, long startDate, long endDate);
}
