package ua.foxminded.schoolconsoleapp.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.DAO;
import ua.foxminded.schoolconsoleapp.exception.DomainException;

public abstract class AbstractJdbcDAO<T> implements DAO<T, Integer> {
    private static final String RESULT_OF_UPDATE = "Update is failed!";
    private static final String RESULT_OF_INSERT = "Insertion is failed!";

    protected final ConnectionProvider connectionProvider;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractJdbcDAO(ConnectionProvider connectionProvider, String saveQuery, String findByIdQuery, String findAllQuery,
	    String updateQuery, String deleteByIdQuery) {
	this.connectionProvider = connectionProvider;
	this.saveQuery = saveQuery;
	this.findByIdQuery = findByIdQuery;
	this.findAllQuery = findAllQuery;
	this.updateQuery = updateQuery;
	this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void add(T entity) {
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(saveQuery)) {
	    insertSave(statement, entity);
	    statement.executeUpdate();
	} catch (SQLException e) {
	    throw new DomainException(RESULT_OF_INSERT, e);
	}
    }

    @Override
    public void deleteById(Integer entityId) {
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(deleteByIdQuery)) {
	    statement.setInt(1, entityId);
	    statement.execute();
	} catch (SQLException e) {
	    throw new DomainException(RESULT_OF_UPDATE, e);
	}
    }

    @Override
    public void update(T entity) {
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(updateQuery)) {
	    insertUpdate(statement, entity);
	    statement.executeUpdate();
	} catch (SQLException e) {
	    throw new DomainException(RESULT_OF_UPDATE, e);
	}
    }

    @Override
    public List<T> findAll(int page, int itemsPerPage) {
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(findAllQuery)) {
	    List<T> entities = new ArrayList<>();
	    
	    statement.setInt(1, itemsPerPage);
	    statement.setInt(2, page);
	    final ResultSet resultSet = statement.executeQuery();

	    while (resultSet.next()) {
		entities.add(mapResultSetToEntity(resultSet));
	    }

	    return entities;
	} catch (SQLException e) {
	    throw new DomainException(RESULT_OF_UPDATE, e);
	}
    }
    
    @Override
    public Optional<T> findById(Integer entityId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(findByIdQuery)) {
            statement.setInt(1, entityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DomainException(e);
        }
    }

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    public abstract void insertSave(PreparedStatement statement, T entity) throws SQLException;

    public abstract void insertUpdate(PreparedStatement statement, T entity) throws SQLException;
}
