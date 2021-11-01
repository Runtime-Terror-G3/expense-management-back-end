import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class UserRepository implements IUserRepository {
    private SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findOne(Integer integer) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            User user = session.createQuery("from User where id=?", User.class).setParameter(0, integer).setMaxResults(1).uniqueResult();
            session.getTransaction().commit();

            return user;
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();

            return users;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(User entity) {
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
        User user = findOne(integer);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }

    @Override
    public void update(User entity) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            User user = (User) session.load(User.class, entity.getId());
            user.setEmail(entity.getEmail());
            user.setFirstName(entity.getFirstName());
            user.setLastName(entity.getLastName());
            user.setDateOfBirth(entity.getDateOfBirth());
            user.setPasswordHash(entity.getPasswordHash());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            throw e;
        }
    }
}
