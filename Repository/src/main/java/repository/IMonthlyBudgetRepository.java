package repository;

import domain.MonthlyBudget;

import java.util.Date;

public interface IMonthlyBudgetRepository extends IRepository<Integer, MonthlyBudget> {
    /**
     * Get the monthly budgets for a user filtered by a date interval
     * @param userId id of the user the expenses belong to
     * @param startDate start date of the interval
     * @param endDate end date of the interval
     * @return a collection containing the requested expenses
     */
    Iterable<MonthlyBudget> findByFilter(int userId, Date startDate, Date endDate);
}
