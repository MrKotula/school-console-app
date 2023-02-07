package ua.foxminded.schoolconsoleapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public class StartsDao {
    private static final String FILENAME_SCRIPT_CREATE_TABLES = "schema.sql";
    private static final String MESSAGE_TABLES_READY = "Tables are ready";

    private final ConnectionProvider connectionProvider;

    public StartsDao(ConnectionProvider connectionProvider) throws DaosException {
	this.connectionProvider = connectionProvider;
	prepareTable();
    }

    public void prepareTable() throws DaosException {
	createTable(FILENAME_SCRIPT_CREATE_TABLES);
	System.out.println(MESSAGE_TABLES_READY);
    }

    private void createTable(String scriptFilename) throws DaosException {
	try (Connection connection = connectionProvider.getConnection()) {
	    SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
	    scriptRunner.runSqlScript(scriptFilename);
	} catch (SQLException e) {
	    throw new DaosException("Can't run script", e);
	}
    }
}
