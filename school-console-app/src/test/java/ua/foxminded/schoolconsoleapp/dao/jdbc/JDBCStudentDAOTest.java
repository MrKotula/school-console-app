package ua.foxminded.schoolconsoleapp.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.SqlScriptRunner;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

class JDBCStudentDAOTest {
    private static final String PATH_TO_CREATE_SCHEMA_TEST = "test_schema.sql";
    private static final String PATH_TO_DELETE_SCHEMA_TEST = "drop_all_tables.sql";

    private static final String PROPERTY_STUDENT_GET_WITH_COURSE_NAME = "SELECT student_id, group_id, first_name, last_name "
	    + "FROM schedule.students " + "INNER JOIN (students_courses INNER JOIN schedule.courses USING (course_id)) "
	    + "USING (student_id) " + "WHERE courses.course_name = ? ";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM students_courses WHERE student_id = ? and course_id = ?";

    private static final String MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE = "Can't delete student %d from course %d";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get students";

    private ConnectionProvider connectionProvider;
    private JDBCStudentDAO jdbcStudentDAO;
    private SqlScriptRunner sqlScriptRunner;
    private Student studentTestFirst;

    @BeforeEach
    void generateTestData() throws DAOException, SQLException {
	connectionProvider = new ConnectionProvider();
	jdbcStudentDAO = new JDBCStudentDAO(connectionProvider);
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
    void saveShouldBeSaveStudentOnDataBase() {
	Student expected = new Student(1, "John", "Doe");
	jdbcStudentDAO.add(expected);

	assertEquals(expected, jdbcStudentDAO.findById(1).get());
    }

    @Test
    void givenStudents_whenRemoveStudentId2FromCourseId2_thenRemoveRecord() throws DAOException {
	int studentId = 2;
	jdbcStudentDAO.removeStudentFromCourse(studentId, 2);
	List<Student> students = jdbcStudentDAO.getStudentsWithCourseName("biology");
	Student actualStudent = students.stream().filter(s -> s.getGroupId() == studentId).findAny().orElse(null);
	assertNull(actualStudent);
    }

    @Test
    void givenStudentWithGroup_whenUpdateStudent1WithGroupId_thenReturnNewStudentWithGroupId() {
	studentTestFirst = new Student(1, 1, "John", "Doe");
	jdbcStudentDAO.update(studentTestFirst);

	assertEquals(studentTestFirst.getLastName(), jdbcStudentDAO.findById(1).get().getLastName());
    }

    @Test
    void givenStudents_whenGetStudentsWithCourseMath_thenReturnStudent() throws DAOException {
	List<Student> students = new ArrayList<>();
	studentTestFirst = new Student(1, 1, "John", "Doe");
	students.add(studentTestFirst);
	jdbcStudentDAO.addStudentCourse(1, 1);

	assertEquals(students, jdbcStudentDAO.getStudentsWithCourseName("math"));

	jdbcStudentDAO.removeStudentFromCourse(1, 1);
    }

    @Test
    void shouldReturnDaoExceptionWhengetStudents() throws DAOException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_GET_WITH_COURSE_NAME)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	Exception exception = assertThrows(DAOException.class,
		() -> jdbcStudentDAOMocked.getStudentsWithCourseName("math"));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionWhenRemoveStudents() throws DAOException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_COURSE_DELETE)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	String expectedMessage = String.format(MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE, 1, 2);
	Exception exception = assertThrows(DAOException.class,
		() -> jdbcStudentDAOMocked.removeStudentFromCourse(1, 2));

	assertEquals(expectedMessage, exception.getMessage());
    }
}
