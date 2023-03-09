package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.schoolconsoleapp.exception.DAOException;

public interface DAO<T, ID> {
    void add(T entity);
    
    Optional<T> findById(ID id) throws DAOException;
    
    List<T> findAll(int page, int itemsPerPage);
    
    void update(T entity);
    
    void deleteById(ID id);
}
