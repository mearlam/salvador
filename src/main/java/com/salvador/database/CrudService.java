package com.salvador.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExcludeDefaultInterceptors
public class CrudService {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private static final String PERSISTENT_UNIT = "testapp";

    private static EntityManagerFactory emf = null;

    EntityManager em;

    public CrudService() {
        initialise();
    }

    private synchronized void initialise() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENT_UNIT);
        }

        log.debug("Creating entity manager");
        em = emf.createEntityManager();
    }

    @PreDestroy
    private void commit() {
        try {
            if (em.getTransaction().isActive()) {

                if (em.getTransaction().getRollbackOnly()) {
                    em.getTransaction().rollback();
                } else {
                    em.getTransaction().commit();
                }
            }

        } finally {
            em.close();
            em = null;
        }
    }

    /**
     * Persists a new object to the database
     *
     * @param t new object
     * @return new object with any modifications
     */
    public <T extends Serializable> T create(T t) {
        beginTransaction();
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);
        return t;
    }

    /**
     * Retrieves an object from the database
     *
     * @param type {@link Class} of the object
     * @param pk   object's primary key
     * @return object
     */
    public <T extends Serializable> T find(Class<T> type, Serializable pk) {
        return this.em.find(type, pk);
    }

    /**
     * Updates an object on the database
     *
     * @param t object to be updated
     * @return object to be updated with any modifications
     */
    public <T extends Serializable> T update(T t) {
        beginTransaction();
        return this.em.merge(t);
    }

    /**
     * Deletes a previously retrieved object from the database
     *
     * @param obj object to be deleted
     */
    public void delete(Serializable obj) {
        beginTransaction();
        this.em.remove(obj);
        this.em.flush();
    }

    /**
     * Deletes an object from the database by primary key
     *
     * @param type {@link Class} of the object to be deleted
     * @param pk   primary key of the object to be deleted
     */
    public void delete(Class<? extends Serializable> type, Serializable pk) {
        beginTransaction();
        final Object ref = this.em.getReference(type, pk);
        this.em.remove(ref);
        this.em.flush();
    }

    private void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            this.em.getTransaction().begin();
        }
    }

    /**
     * Retrieves a list of objects from the database using a named query
     *
     * @param queryName named query
     * @return list of objects
     */
    @SuppressWarnings({"NullableProblems"})
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName) {
        return findWithNamedQuery(queryName, null, 0, 0);
    }

    /**
     * Retrieves a list of objects from the database using a named query
     *
     * @param queryName  named query
     * @param parameters parameters
     * @return list of objects
     */
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName, QueryParameters parameters) {
        return findWithNamedQuery(queryName, parameters, 0, 0);
    }

    /**
     * Retrieves a list of objects from the database using a named query
     *
     * @param queryName   named query
     * @param resultLimit if greater than zero, the returned list is limited to this number of elements
     * @return list of objects
     */
    @SuppressWarnings({"NullableProblems"})
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName, int resultLimit) {
        return findWithNamedQuery(queryName, null, resultLimit, 0);
    }

    @SuppressWarnings({"NullableProblems"})
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName, QueryParameters parameters, int resultLimit) {
        return findWithNamedQuery(queryName, parameters, resultLimit, 0);
    }

    @SuppressWarnings({"NullableProblems"})
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName, int resultLimit, int firstResult) {
        return findWithNamedQuery(queryName, null, resultLimit, firstResult);
    }

    /**
     * Retrieves a list of objects from the database using a named query
     *
     * @param queryName   named query
     * @param parameters  parameters
     * @param resultLimit if greater than zero, the returned list is limited to this number of elements
     * @param firstResult if greater than zero, the returned list starts from the nth query result
     * @return list of objects
     */
    @SuppressWarnings({"unchecked"})
    public <T extends List<? extends Serializable>> T findWithNamedQuery(String queryName, QueryParameters parameters, int resultLimit, int firstResult) {
        final Query query = this.em.createNamedQuery(queryName);
        applyParameters(query, parameters);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        query.setFirstResult(firstResult);
        return (T) query.getResultList();
    }

    /**
     * Retrieves an object from the database using a named query
     *
     * @param queryName  named query
     * @param parameters parameters
     * @return single object
     * @throws javax.persistence.NonUniqueResultException
     *          if query returns more than one result
     */
    @SuppressWarnings({"unchecked"})
    public <T extends Serializable> T findUniqueWithNamedQuery(String queryName, QueryParameters parameters) {
        final Query query = this.em.createNamedQuery(queryName);
        applyParameters(query, parameters);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T findUniqueWithNamedQuery(String queryName) {
        final Query query = this.em.createNamedQuery(queryName);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Executes an update or delete statement
     *
     * @param queryName  named query
     * @param parameters parameters
     * @return number of rows deleted or updated
     */
    public int executeNamedStatement(String queryName, QueryParameters parameters) {
        final Query query = this.em.createNamedQuery(queryName);
        applyParameters(query, parameters);
        return query.executeUpdate();
    }

    private void applyParameters(Query query, QueryParameters parameters) {
        if (parameters != null) {
            for (final Map.Entry<String, Object> parameter : parameters.entries()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
    }

    /**
     * Clears all of the objects from the cache. ONLY to be called
     * when somebody really wants to clear the cache and for no
     * other reason
     */
    public void clearCache() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    /**
     * Retrieves a list of objects from the database using a raw SQL
     *
     * @param sqlString  SQL string
     * @param parameters a {@link java.util.Map} of parameter names to parameter values
     * @return list of objects
     */
    @SuppressWarnings("unchecked")
    public int executeNativeQuery(String sqlString, Map<String, Object> parameters) {
        final Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        final Query query = this.em.createNativeQuery(sqlString);
        for (final Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    // for unit testing
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}

