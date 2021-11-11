package repository.hibernate;

import domain.Expense;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;

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
}
