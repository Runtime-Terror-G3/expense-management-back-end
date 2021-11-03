import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class ExpenseRepository implements IExpenseRepository {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public ExpenseRepository(){ }

    @Override
    public Expense findOne(Integer integer) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Expense expense = session.createQuery("from Expense where id=?", Expense.class).setParameter(0, integer).setMaxResults(1).uniqueResult();
            session.getTransaction().commit();

            return expense;
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<Expense> findAll() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            List<Expense> expenses = session.createQuery("from Expense ", Expense.class).list();
            session.getTransaction().commit();

            return expenses;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Expense entity) {
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
        Expense expense = findOne(integer);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.delete(expense);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }

    @Override
    public void update(Expense entity) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Expense expense = (Expense) session.load(Expense.class, entity.getId());
            expense.setAmount(entity.getAmount());
            expense.setCategory(entity.getCategory());
            expense.setDate(entity.getDate());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }
}
