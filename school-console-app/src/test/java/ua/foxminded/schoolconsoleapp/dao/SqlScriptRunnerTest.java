package ua.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

class SqlScriptRunnerTest {
    private static final String MASK_EXCEPTION = "Script %s not found!";
    
    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private SqlScriptRunner sqlScriptRunner;
    
    @Test
    void shouldReturnDaoExceptionIfScriptIsNull( ) throws DAOException, SQLException {
	Connection connection = connectionProvider.getConnection(); 
	sqlScriptRunner = new SqlScriptRunner(connection);
	Exception exception = assertThrows(DAOException.class, () -> sqlScriptRunner.runSqlScript(null));
	String expectedMessage = String.format(MASK_EXCEPTION, null);
	
	assertEquals(expectedMessage, exception.getMessage());	
    }
}
