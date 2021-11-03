package repository.hibernate;

import domain.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.IUserRepository;

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
}
