package ca.jc2brown.mmdb.dao;
import java.util.List;

public interface GenericDai<T> {
 
    T findById(Long id, boolean lock);
 
    List<T> findAll();
 
    List<T> findByExample(T exampleInstance, String[] excludeProperty);
 
    T makePersistent(T entity);
 
    void makeTransient(T entity);
}