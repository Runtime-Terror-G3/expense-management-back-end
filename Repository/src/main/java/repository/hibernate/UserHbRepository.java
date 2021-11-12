package repository.hibernate;

import domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IUserRepository;
import java.util.Optional;

@Component
public class UserHbRepository extends AbstractHbRepository<Integer, User> implements IUserRepository {
    @Override
    protected Query<User> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from User where id=:id", User.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<User> getFindAllQuery(Session session) {
        return session.createQuery("from User", User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User entity = session.createQuery("from User where email=:email", User.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }

        return Optional.empty();
    }
}
