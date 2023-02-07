package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    void add(T entity);
    
    Optional<T> findById(ID id);
    
    List<T> findAll(int page, int itemsPerPage);
    
    void update(T entity);
    
    void deleteById(ID id);
}
