package repository.hibernate;

import domain.MonthlyBudget;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IMonthlyBudgetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class MonthlyBudgetHbRepository extends AbstractHbRepository<Integer, MonthlyBudget> implements IMonthlyBudgetRepository {
    @Override
    protected Query<MonthlyBudget> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from MonthlyBudget where id=:id", MonthlyBudget.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<MonthlyBudget> getFindAllQuery(Session session) {
        return session.createQuery("from MonthlyBudget", MonthlyBudget.class);
    }

    @Override
    public Iterable<MonthlyBudget> findByFilter(int userId, Date startDate, Date endDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<MonthlyBudget> sqlQuery;

            String sqlQueryString = "select new domain.MonthlyBudget(id, income, date) " +
                    "from MonthlyBudget where userid=:userId " +
                    "and date between :startDate and :endDate " +
                    "order by date desc";

            sqlQuery = session.createQuery(sqlQueryString, MonthlyBudget.class);

            List<MonthlyBudget> monthlyBudgets = sqlQuery
                    .setParameter("userId", userId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
            transaction.commit();
            return monthlyBudgets;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return new ArrayList<>();
    }
}
