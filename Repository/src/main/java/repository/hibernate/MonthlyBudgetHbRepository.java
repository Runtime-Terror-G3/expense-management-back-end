package repository.hibernate;

import domain.MonthlyBudget;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IMonthlyBudgetRepository;

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
}
