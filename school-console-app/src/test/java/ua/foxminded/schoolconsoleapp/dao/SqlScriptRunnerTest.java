package ua.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

class SqlScriptRunnerTest {
    private static final String FILEPATH_DB_PROPERTIES = "src/test/resources/db.propertiesTest.properties";
    private static final String MASK_EXCEPTION = "Script %s not found!";

    private ConnectionProvider connectionProvider;
    private SqlScriptRunner sqlScriptRunner;

    @BeforeEach
    void setup() throws SQLException {
	connectionProvider = new ConnectionProvider(FILEPATH_DB_PROPERTIES);
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);
    }

    @Test
    void shouldReturnDaoExceptionIfScriptIsNull() throws DaosException, SQLException, ClassNotFoundException {
	Exception exception = assertThrows(DaosException.class, () -> sqlScriptRunner.runSqlScript(null));
	String expectedMessage = String.format(MASK_EXCEPTION, null);

	assertEquals(expectedMessage, exception.getMessage());
    }
}
