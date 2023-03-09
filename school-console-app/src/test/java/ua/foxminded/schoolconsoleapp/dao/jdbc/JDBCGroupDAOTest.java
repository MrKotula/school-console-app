package ua.foxminded.schoolconsoleapp.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.SqlScriptRunner;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

class JDBCGroupDAOTest {
    private static final String PATH_TO_CREATE_SCHEMA_TEST = "test_schema.sql";
    private static final String PATH_TO_DELETE_SCHEMA_TEST = "drop_all_tables.sql";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get groups";
    private static final String PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT = "SELECT groups.group_id, groups.group_name, COUNT(student_id) "
	    + "FROM schedule.groups " + "LEFT JOIN schedule.students ON groups.group_id = students.group_id "
	    + "GROUP BY groups.group_id, groups.group_name " + "HAVING COUNT(student_id) <= ?";

    private ConnectionProvider connectionProvider;
    private JDBCGroupDAO jdbcGroupDAO;
    private SqlScriptRunner sqlScriptRunner;
    private Group testGroup;
    private Group testGroupFirst = Group.builder()
		.withGroupId(1)
		.withGroupName("OR-41")
		.build();
	
    private Group testGroupSecond = Group.builder()
		.withGroupId(2)
		.withGroupName("GM-87")
		.build();
	
    private Group testGroupThird = Group.builder()
		.withGroupId(3)
		.withGroupName("XI-12")
		.build();

    @BeforeEach
    void generateTestData() throws DAOException, SQLException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider();
	jdbcGroupDAO = new JDBCGroupDAO(connectionProvider);
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);

	sqlScriptRunner.runSqlScript(PATH_TO_CREATE_SCHEMA_TEST);
    }

    @AfterEach
    void cleanTestData() throws DAOException, SQLException {
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);

	sqlScriptRunner.runSqlScript(PATH_TO_DELETE_SCHEMA_TEST);
    }

    @Test
    void findAllEntityShouldBeReturnAllEntityFromDataBase() {
	List<Group> expected = Arrays.asList(testGroupFirst, testGroupSecond, testGroupThird);

	assertEquals(expected, jdbcGroupDAO.findAll(0, 3));
    }

    @Test
    void givenGroup_whenAddGroup_thenReturnAddedGroup() throws DAOException {
	testGroup = Group.builder()
		.withGroupId(4)
		.withGroupName("s")
		.build();
	
	jdbcGroupDAO.add(testGroup);

	assertEquals(testGroup, jdbcGroupDAO.findById(4).get());
    }

    @Test
    void deleteEntityShouldBeDeleteEntityFromDataBase() throws DAOException {
	jdbcGroupDAO.deleteById(5);
	Optional<Group> expected = Optional.empty();

	assertEquals(expected, jdbcGroupDAO.findById(5));
    }

    @Test
    void updateEntityShouldBeUpdateDataIntoDatabase() throws DAOException {
	testGroup = Group.builder()
		.withGroupId(3)
		.withGroupName("S")
		.build();

	jdbcGroupDAO.update(testGroup);

	assertEquals(testGroup, jdbcGroupDAO.findById(3).orElse(null));
    }

    @Test
    void findLessQuantityStudentsShouldBeReturnGroupsWithLessQuantityStudents() throws DAOException {
	List<Group> expected = Arrays.asList(testGroupSecond,testGroupThird, testGroupFirst);

	assertEquals(expected, jdbcGroupDAO.getGroupsWithLessEqualsStudentCount(2));
    }

    @Test
    void shouldReturnDaoExceptionWhenCountStudents() throws DAOException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT)).thenThrow(new SQLException());
	JDBCGroupDAO jdbcGroupDAOMocked = new JDBCGroupDAO(connectionProviderMocked);
	
	String expectedMessage = String.format(MESSAGE_EXCEPTION_GET_ALL, 1);
	Exception exception = assertThrows(DAOException.class, () -> jdbcGroupDAOMocked.getGroupsWithLessEqualsStudentCount(2));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
}
