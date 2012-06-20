package ca.jc2brown.mmdb.dao;

import java.util.List;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class GenericDao<T> extends HibernateDaoSupport implements GenericDai<T> {
 
    private Class<T> persistentClass;
 
    @SuppressWarnings("unchecked")
	public GenericDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
 
    @SuppressWarnings({ "unchecked", "deprecation" })
    public T findById(Long id, boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        } else {
            entity = (T) getSession().load(getPersistentClass(), id);
        }
        return entity;
    }
 
    public List<T> findAll() {
        return findByCriteria();
    }
 
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    @Transactional
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }
    @Transactional
    public void makeTransient(T entity) {
        getSession().delete(entity);
    }
 
    public void flush() {
        getSession().flush();
    }
 
    public void clear() {
        getSession().clear();
    }
 
    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
   }
 
}