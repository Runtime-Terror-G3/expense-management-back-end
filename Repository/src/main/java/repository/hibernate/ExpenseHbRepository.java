package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import domain.TotalExpensesDto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ExpenseHbRepository extends AbstractHbRepository<Integer, Expense> implements IExpenseRepository {
    @Override
    protected Query<Expense> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from Expense where id=:id", Expense.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<Expense> getFindAllQuery(Session session) {
        return session.createQuery("from Expense", Expense.class);
    }

    @Override
    public Iterable<Expense> findByFilter(int userId, String category, long startDate, long endDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Expense> sqlQuery;

            String sqlQueryString = "select new domain.Expense(id, amount, category, date) " +
                    "from Expense where userid=:userId " +
                    "and date between :startDate and :endDate ";

            if (!category.equalsIgnoreCase("ALL")) {
                sqlQueryString += "and category=:category order by date desc";
                sqlQuery = session.createQuery(sqlQueryString, Expense.class)
                    .setParameter("category", ExpenseCategory.valueOf(category));
            } else {
                sqlQueryString += "order by date desc";
                sqlQuery = session.createQuery(sqlQueryString, Expense.class);
            }
            List<Expense> expenses = sqlQuery
                    .setParameter("userId", userId)
                    .setParameter("startDate",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(startDate),
                                TimeZone.getDefault().toZoneId()))
                    .setParameter("endDate",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(endDate),
                                TimeZone.getDefault().toZoneId()))
                    .list();
            transaction.commit();
            return expenses;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return new ArrayList<>();
    }

    @Override
    public Iterable<TotalExpensesDto> findTotalExpenseInTimeByGranularity(int userId, String granularity, LocalDate startDate, LocalDate endDate, String category) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<TotalExpensesDto> sqlQuery;

            String sqlQueryString = "select new domain.TotalExpensesDto(SUM(amount), MAX(date)) " +
                    "from Expense where userid=:userId and date between :startDate and :endDate ";

            category = category.substring(0,1).toUpperCase() + category.substring(1).toLowerCase();
            if (category.equalsIgnoreCase("All")) {
                sqlQueryString += "group by " + granularity + "(date)" + " order by MAX(date) desc";
                sqlQuery = session.createQuery(sqlQueryString, TotalExpensesDto.class);
            } else {
                sqlQueryString += "and category=:category group by " + granularity + "(date)" + " order by MAX(date) desc";
                sqlQuery = session.createQuery(sqlQueryString, TotalExpensesDto.class);
                sqlQuery.setParameter("category", ExpenseCategory.valueOf(category));
            }

            List<TotalExpensesDto> expenses = sqlQuery.setParameter("userId", userId)
                    .setParameter("startDate", startDate.atStartOfDay())
                    .setParameter("endDate", endDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)).list();
            transaction.commit();
            return expenses;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
