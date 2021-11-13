package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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
    public Optional<Iterable<Expense>> findByFilter(int userId, String category, long startDate, long endDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Expense> sqlQuery;

            String sqlQueryString = "select new domain.Expense(id, amount, category, date) " +
                    "from Expense where userid=:userId " +
                    "and date between :startDate and :endDate ";

            if (! category.equalsIgnoreCase("ALL")) {
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
            return Optional.of(expenses);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return Optional.empty();
    }
}
