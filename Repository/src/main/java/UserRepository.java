import org.hibernate.cfg.NotYetImplementedException;

public class UserRepository implements IUserRepository{
    @Override
    public User findOne(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public Iterable<User> findAll() {
        throw new NotYetImplementedException();
    }

    @Override
    public void save(User entity) {
        throw new NotYetImplementedException();
    }

    @Override
    public void delete(Integer integer) {
        throw new NotYetImplementedException();
    }

    @Override
    public void update(User entity) {
        throw new NotYetImplementedException();
    }
}
