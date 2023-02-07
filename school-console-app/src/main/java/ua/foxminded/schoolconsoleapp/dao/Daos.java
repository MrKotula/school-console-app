package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public interface Daos<T, ID> {
    void add(T entity) throws DaosException;

    Optional<T> findById(ID id) throws DaosException;

    List<T> findAll(int page, int itemsPerPage) throws DaosException;

    void update(T entity) throws DaosException;

    void deleteById(ID id) throws DaosException;
}
