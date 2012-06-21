package ca.jc2brown.generic.dao;
import java.util.List;

import org.hibernate.SessionFactory;

public interface GenericDai<T> {
 
    T findById(Long id, boolean lock);
 
    List<T> findAll();
 
    List<T> findByExample(T exampleInstance, String[] excludeProperty);
 
    T makePersistent(T entity);
 
    void makeTransient(T entity);

	void setSessionFactory(SessionFactory sessionFactory);
}