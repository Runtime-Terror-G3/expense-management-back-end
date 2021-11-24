package repository;

import domain.Expense;
import domain.TotalExpensesDto;

import java.time.LocalDate;

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

    Iterable<TotalExpensesDto> findTotalExpenseInTimeByGranularity(int userId, String granularity, LocalDate startDate, LocalDate endDate, String category);
}
