public interface IRepository<ID, E extends Entity<ID>> {
    /**
     *
     * @param id -the id of the entity to be returned
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     */
    E findOne(ID id);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *
     * @param entity - entity to be saved
     * @throws Exception if the entity couldn't be saved.
     */
    void save(E entity);

    /**
     *  removes the entity with the specified id
     * @param id - the id of the entity to be removed
     * @throws Exception if the entity couldn't be removed.
     */
    void delete(ID id);

    /**
     *
     * @param entity - the entity with updated fields
     * @throws Exception if the entity couldn't be updated.
     */
    void update(E entity);
}
