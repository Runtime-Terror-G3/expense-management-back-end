import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class MonthlyBudgetRepository implements IMonthlyBudgetRepository {
    private SessionFactory sessionFactory;

    public MonthlyBudgetRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MonthlyBudget findOne(Integer integer) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            MonthlyBudget budget = session.createQuery("from MonthlyBudget where id=?", MonthlyBudget.class).setParameter(0, integer).setMaxResults(1).uniqueResult();
            session.getTransaction().commit();

            return budget;
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<MonthlyBudget> findAll() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            List<MonthlyBudget> budgets = session.createQuery("from MonthlyBudget ", MonthlyBudget.class).list();
            session.getTransaction().commit();

            return budgets;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(MonthlyBudget entity) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }

    @Override
    public void delete(Integer integer) {
        MonthlyBudget budget = findOne(integer);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.delete(budget);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }

    @Override
    public void update(MonthlyBudget entity) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            MonthlyBudget budget = (MonthlyBudget) session.load(MonthlyBudget.class, entity.getId());
            budget.setIncome(entity.getIncome());
            budget.setDate(entity.getDate());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }
}
