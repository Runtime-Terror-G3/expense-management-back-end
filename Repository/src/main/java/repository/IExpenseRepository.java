package repository;

import domain.Expense;
import domain.ExpenseCategory;
import domain.User;

import java.time.LocalDateTime;
import java.util.Map;

public interface IExpenseRepository extends IRepository<Integer, Expense> {
    /**
     * Get the expenses for a user filtered by a date interval
     * @param userId id of the user the expenses belong to
     * @param category category of the expenses
     * @param startDate UNIX timestamp representing the start date of the interval
     * @param endDate UNIX timestamp representing the end date of the interval
     * @return a collection containing the requested expenses
     */
    Iterable<Expense> findByFilter(int userId, String category, long startDate, long endDate);

    /**
     * Method for retrieving statistics regarding the total amount of expenses of a user by category within a time period
     * @param user, the user the statistics are addressed to
     * @param start, the start of the considered time period
     * @param end, the end of the considered time period
     * @return a {@code Map} containing, for each ExpenseCategory present, the total amount
     */
    Map<ExpenseCategory, Double> getTotalAmountByCategory(User user, LocalDateTime start, LocalDateTime end);
}
