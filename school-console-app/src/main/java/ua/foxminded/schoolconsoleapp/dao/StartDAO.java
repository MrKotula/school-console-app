package ua.foxminded.schoolconsoleapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

public class StartDAO {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String DELIMITER_QUERIES = ";";

    private static final String PROPERTY_SQL_DELETE_TABLES = "DROP SCHEMA IF EXISTS schedule CASCADE; " + LINE_SEPARATOR + 
	    "DROP TABLE IF EXISTS schedule.groups CASCADE; " + LINE_SEPARATOR
	    + "DROP TABLE IF EXISTS schedule.students CASCADE; " + LINE_SEPARATOR + "DROP TABLE IF EXISTS schedule.courses CASCADE;"
	    + LINE_SEPARATOR + "DROP TABLE IF EXISTS students_courses CASCADE;";

    private static final String FILENAME_SCRIPT_CREATE_TABLES = "schema.sql";
    private static final String MESSAGE_TABLES_READY = "Tables are ready";
    
    private ConnectionProvider connectionProvider;
    
    public StartDAO() throws ClassNotFoundException, DAOException {
	prepareTable();
    }

    public void prepareTable() throws DAOException, ClassNotFoundException {
	deleteTable();
	createTable(FILENAME_SCRIPT_CREATE_TABLES);
	System.out.println(MESSAGE_TABLES_READY);
    }

    private void deleteTable() throws DAOException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider();
	String sqlCommands = PROPERTY_SQL_DELETE_TABLES;

	String[] sql = sqlCommands.split(DELIMITER_QUERIES);

	try (Connection connection = connectionProvider.getConnection()) {
	    try (Statement statement = connection.createStatement()) {

		connection.setAutoCommit(false);
		for (int i = 0; i < sql.length; i++) {
		    statement.addBatch(sql[i]);
		}
		statement.executeBatch();
		connection.commit();
	    }
	} catch (SQLException e) {
	    throw new DAOException();
	}
    }

    private void createTable(String scriptFilename) throws DAOException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider();
	try (Connection connection = connectionProvider.getConnection()) {

	    SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
	    scriptRunner.runSqlScript(scriptFilename);

	} catch (SQLException e) {
	    throw new DAOException("Can't run script", e);
	}
    }
}
