package ua.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCCourseDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCGroupDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCStudentDAO;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

class StartDAOTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    
    private static final String PATH_TEST_SCHEMA = "test_schema.sql";
    private static final String PATH_TEST_DROP_TABLES = "drop_all_tables.sql";
    private static final String PROPERTY_SQL_DELETE_TABLES = "DROP SCHEMA IF EXISTS schedule CASCADE; " + LINE_SEPARATOR + 
	        "DROP TABLE IF EXISTS schedule.groups CASCADE; " + LINE_SEPARATOR
	        + "DROP TABLE IF EXISTS schedule.students CASCADE; " + LINE_SEPARATOR + "DROP TABLE IF EXISTS schedule.courses CASCADE;"
	        + LINE_SEPARATOR + "DROP TABLE IF EXISTS students_courses CASCADE;";

    private static final String EXCEPTION_MASSEGE_RUN_SCRIPT = "Can't run script";

    private StartDAO startDAO;
    private ConnectionProvider connectionProvider;

    @BeforeEach
    void setUp() throws Exception {
	startDAO = new StartDAO();
	connectionProvider = new ConnectionProvider();
	try (Connection connection = connectionProvider.getConnection()) {
	    SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
	    scriptRunner.runSqlScript(PATH_TEST_SCHEMA);
	} catch (SQLException e) {
	    throw new DAOException(e.getMessage());
	}
    }

    @AfterEach
    void tearDown() throws Exception {
	connectionProvider = new ConnectionProvider();
	try (Connection connection = connectionProvider.getConnection()) {
	    SqlScriptRunner scriptRunner = new SqlScriptRunner(connection);
	    scriptRunner.runSqlScript(PATH_TEST_DROP_TABLES);
	} catch (SQLException e) {
	    throw new DAOException(e.getMessage());
	}
    }

    @Test
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableGroupsCreated() throws DAOException, ClassNotFoundException {
        startDAO = new StartDAO();

        List<Group> expectedGroups = new ArrayList<>();
        JDBCGroupDAO groupDao = new JDBCGroupDAO(connectionProvider);
        List<Group> actualGroups = groupDao.getGroupsWithLessEqualsStudentCount(1);

        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableStudentsCreated() throws DAOException, ClassNotFoundException {
	startDAO = new StartDAO();

        List<Student> expectedGroups = new ArrayList<>();
        JDBCStudentDAO studentDAO = new JDBCStudentDAO(connectionProvider);
        List<Student> actualGroups = studentDAO.getStudentsWithCourseName("math");

        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void givenPrepareTablesMethod_whenGetAll_thenEmptyTableCoursesCreated() throws DAOException, ClassNotFoundException {
	startDAO = new StartDAO();

        List<Course> expectedGroups = new ArrayList<>();
        JDBCCourseDAO courseDAO = new JDBCCourseDAO(connectionProvider);
        List<Course> actualGroups = courseDAO.getCoursesForStudentId(1);

        assertEquals(expectedGroups, actualGroups);
    }
    
    @Test
    void shouldThrowDaoExceptionIfSQLScriptNotFound() throws SQLException, DAOException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.createStatement()).thenThrow(new SQLException());
	SqlScriptRunner sqlScriptRunner = new SqlScriptRunner(connectionMocked);
	
	Exception exception = assertThrows(DAOException.class, () -> sqlScriptRunner.runSqlScript(PROPERTY_SQL_DELETE_TABLES));

	assertEquals(String.format("Script %s not found!", PROPERTY_SQL_DELETE_TABLES), exception.getMessage());
    }
    
    @Test
    void shouldThrowDaoExceptionWhenPrepareTable() throws SQLException, DAOException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.createStatement()).thenThrow(new SQLException(EXCEPTION_MASSEGE_RUN_SCRIPT));
	connectionProviderMocked.getConnection();	
	
	Exception exception = assertThrows(SQLException.class, () -> connectionMocked.createStatement());

	assertEquals(EXCEPTION_MASSEGE_RUN_SCRIPT, exception.getMessage());
    }
}
