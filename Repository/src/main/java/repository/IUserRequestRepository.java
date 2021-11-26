package repository;

import domain.UserRequest;

import java.util.Optional;

public interface IUserRequestRepository extends IRepository<Integer, UserRequest>{

    /**
     * Method for finding a UserRequest by its activation token
     * @param activationToken: String, activationToken of the userRequest
     * @return an {@code Optional}
     *          - null, if there is no entity with activationToken equal to {@param activationToken}
     *          - the entity, otherwise
     * @throws IllegalArgumentException if activationToken is null
     */
    Optional<UserRequest> findByActivationToken(String activationToken);

    /**
     * Method for saving a UserRequest if it doesn't exist or updating it if already exists (a UserRequest is identified by email)
     * @param userRequest: the UserRequest to be saved or updated
     * @return an {@code Optional}
     *          - null, if the userRequest was successfully saved or updated
     *          - the userRequest (email already exists)
     * @throws IllegalArgumentException if userRequest is null
     */
    Optional<UserRequest> saveOrUpdate(UserRequest userRequest);
}
