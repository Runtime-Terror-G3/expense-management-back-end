package repository.hibernate;

import domain.UserRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import repository.IUserRequestRepository;

import java.util.Optional;

@Component
public class UserRequestHbRepository extends AbstractHbRepository<Integer, UserRequest> implements IUserRequestRepository {

    @Override
    protected Query<UserRequest> getFindQuery(Session session, Integer integer) {
        return session.createQuery("from UserRequest where id=:id", UserRequest.class)
                .setParameter("id", integer);
    }

    @Override
    protected Query<UserRequest> getFindAllQuery(Session session) {
        return session.createQuery("from UserRequest", UserRequest.class);
    }

    @Override
    public Optional<UserRequest> findByActivationToken(String activationToken) {
        if (activationToken == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserRequest entity = session.createQuery("from UserRequest where activationToken=:activationToken", UserRequest.class)
                    .setParameter("activationToken", activationToken)
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

    @Override
    public Optional<UserRequest> saveOrUpdate(UserRequest userRequest) {
        if (userRequest == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserRequest existingUserRequest = session.createQuery("from UserRequest where email=:email", UserRequest.class)
                    .setParameter("email", userRequest.getEmail())
                    .setMaxResults(1)
                    .uniqueResult();

            if (existingUserRequest == null)
                session.save(userRequest);
            else{
                userRequest.setId(existingUserRequest.getId());
                session.update(userRequest);
            }

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
            return Optional.of(userRequest);
        }

        return Optional.empty();
    }
}
