package repository;

import domain.Entity;

import java.io.Serializable;
import java.util.Optional;

public interface IRepository<ID extends Serializable, E extends Entity<ID>> {

    /**
     * Method for saving an entity
     * @param entity: E, the entity to be saved
     * @return an {@code Optional}
     *          - null, if it was successfully saved
     *          - the entity (id already exists)
     * @throws IllegalArgumentException if entity is null
     */
    Optional<E> save(E entity);

    /**
     * Method for deleting an entity by id
     * @param id: ID, the id of the entity to be deleted
     * @return an {@code Optional}
     *          - null, if there is no entity with that id
     *          - the removed entity, otherwise
     * @throws IllegalArgumentException if id is null
     */
    Optional<E> delete(ID id);

    /**
     * Method for updating an entity
     * @param entity: Entity, the entity to be updated
     * @return an {@code Optional}
     *          - null, if the entity was successfully updated
     *          - the entity, otherwise
     * @throws IllegalArgumentException if entity is null
     */
    Optional<E> update(E entity);

    /**
     * Method for finding an entity
     * @param id: ID, id of the desired entity
     * @return an {@code Optional}
     *          - null, if there is no entity with id equal to the id
     *          - the entity, otherwise
     * @throws IllegalArgumentException if id is null
     */
    Optional<E> findOne(ID id);

    /**
     * Method for retrieving all data
     * @return i: Iterable containing all entities
     */
    Iterable<E> findAll();
}