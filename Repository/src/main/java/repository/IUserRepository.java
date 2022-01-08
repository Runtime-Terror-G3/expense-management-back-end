package repository;

import domain.User;

import java.util.Optional;

public interface IUserRepository extends IRepository<Integer, User> {

    /**
     * Method for finding a user
     * @param email: String, email of the desired user
     * @return an {@code Optional}
     *          - null, if there is no entity with email equal to the email
     *          - the entity, otherwise
     * @throws IllegalArgumentException if email is null
     */
    Optional<User> findByEmail(String email);
}
