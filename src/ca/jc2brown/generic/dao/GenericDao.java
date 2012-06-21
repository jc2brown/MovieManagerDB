package ca.jc2brown.generic.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.jc2brown.generic.model.ModelEntity;
import ca.jc2brown.mmdb.MediaManagerDB;

@Repository
public class GenericDao<T extends ModelEntity> extends HibernateDaoSupport implements GenericDai<T> {
	private static Logger log = Logger.getLogger( MediaManagerDB.class.getName() );
 
    private Class<T> persistentClass;
 
    private static Map<Class<?>, GenericDao<? extends ModelEntity>> daoMap;

	private static SessionFactory sessionFactoryHolder;
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static GenericDai<? extends ModelEntity> getDao(Class<? extends ModelEntity> clazz) {
    	if ( daoMap == null ) {
        	daoMap = new HashMap<Class<?>, GenericDao<? extends ModelEntity>>();
    	}
    	GenericDai<? extends ModelEntity> dao = (GenericDai<? extends ModelEntity>) daoMap.get(clazz);
    	if ( dao == null ) {
    		dao = new GenericDao(clazz);
    		dao.setSessionFactory(sessionFactoryHolder);
    		daoMap.put(clazz, (GenericDao<? extends ModelEntity>) dao);
    	}
    	return dao;
    	
    }
    
    
    @SuppressWarnings("unchecked")
	public GenericDao( T entity ) {
    	super(); 
        //this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    	this.persistentClass = (Class<T>) entity.getClass();
    	log.debug(this.persistentClass);
    }
    
    
    public GenericDao(Class<T> clazz) {
    	super();
    	this.persistentClass = clazz;
    	log.debug(this.persistentClass);
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
    	try {
    		getSession().saveOrUpdate(entity);
    	} catch (ConstraintViolationException e) {
    		entity = getAlternate(entity);
    	}
        return entity;
    }
    private T getAlternate(T entity) {
        List<T> alternateEntities = findByExample(entity, new String[] {""});
        if ( alternateEntities.isEmpty() ) {
        	return null;
        }
        return alternateEntities.get(0);
    }
    
    
    @SuppressWarnings("unchecked")
    public static <U extends ModelEntity> U smakePersistent(U entity) {
    	GenericDao<U> dao = ((GenericDao<U>)getDao(entity.getClass()));
    	U persistentEntity = null;
    		log.debug("Attempting to make persistent " + entity);
    		persistentEntity = dao.makePersistent(entity);
    	/*} catch (ConstraintViolationException e) {
    		persistentEntity = (U) dao.findByExample(entity, new String[] {"id"}).get(0);
        	log.debug(" Entity could not be made persistent, replaced by " + persistentEntity);
    	}*/
    	return persistentEntity;
    }
    @SuppressWarnings("unchecked")
    public static <U extends ModelEntity> void smakeTransient(U entity) {
    	GenericDao<U> dao = ((GenericDao<U>)getDao(entity.getClass()));
    	dao.makeTransient(entity);
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
    @Transactional
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
    	Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            log.debug("adding criteria" + c);
            crit.add(c);
        }
        return crit.list();
   }


	public static void setSessionFactoryHolder(SessionFactory sessionFactory) {
		sessionFactoryHolder = sessionFactory;
	}
 
}