package ua.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.schoolconsoleapp.applicationfacade.ApplicationFacade;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

class StartsDaoTest {
    private static final String FILEPATH_DB_PROPERTIES = "src/test/resources/db.propertiesTest.properties";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String PATH_TEST_SCHEMA = "test_schema.sql";
    private static final String PROPERTY_SQL_DELETE_TABLES = "DROP TABLE IF EXISTS schedule.groups CASCADE; " + LINE_SEPARATOR
	    + "DROP TABLE IF EXISTS schedule.students CASCADE; " + LINE_SEPARATOR
	    + "DROP TABLE IF EXISTS schedule.courses CASCADE;" + LINE_SEPARATOR
	    + "DROP TABLE IF EXISTS students_courses CASCADE;";
    private static final String EXCEPTION_MASSEGE_RUN_SCRIPT = "Can't run script";
    private ConnectionProvider connectionProvider;
    private StartsDao startsDao;
    private SqlScriptRunner scriptRunner;
    
    @BeforeEach
    void setUp() throws Exception {
	connectionProvider = new ConnectionProvider(FILEPATH_DB_PROPERTIES);
	try (Connection connection = connectionProvider.getConnection()) {
	    scriptRunner = new SqlScriptRunner(connection);
	    scriptRunner.runSqlScript(PATH_TEST_SCHEMA);
	} catch (SQLException e) {
	    throw new DaosException(e.getMessage());
	}
    }
    
    @Test
    void shouldReturnStartDAOtest() throws DaosException, SQLException {
	startsDao = new StartsDao(connectionProvider);

	assertNotNull(startsDao);
    }

    @Test
    void shouldThrowDaoExceptionIfSQLScriptNotFound() throws SQLException, DaosException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.createStatement()).thenThrow(new SQLException());
	SqlScriptRunner sqlScriptRunner = new SqlScriptRunner(connectionMocked);
	Exception exception = assertThrows(DaosException.class, () -> sqlScriptRunner.runSqlScript(PROPERTY_SQL_DELETE_TABLES));

	assertEquals(String.format("Script %s not found!", PROPERTY_SQL_DELETE_TABLES), exception.getMessage());
    }
}
