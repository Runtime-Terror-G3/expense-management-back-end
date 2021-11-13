package repository.hibernate;

import domain.Expense;
import domain.ExpenseCategory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

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
    public Optional<Iterable<Expense>> findByFilter(int userId, ExpenseCategory expenseCategory, long startDate, long endDate) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            List<Expense> expenses = session.createQuery("select new Expense(amount, category, date) from Expense where userid=:userId and category like :category and date > :startDate and date <= :endDate", Expense.class)
                    .setParameter("userId", userId)
                    .setParameter("category", expenseCategory)
                    .setParameter("startDate", LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC))
                    .setParameter("endDate", LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC))
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
