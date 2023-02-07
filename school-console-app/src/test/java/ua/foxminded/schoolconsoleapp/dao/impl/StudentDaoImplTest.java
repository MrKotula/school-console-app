package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.SqlScriptRunner;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

class StudentDaoImplTest {
    private static final String TEST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String FILEPATH_DB_PROPERTIES = "src/test/resources/db.propertiesTest.properties";
    private static final String PATH_TO_CREATE_SCHEMA_TEST = "test_schema.sql";
    private static final String PROPERTY_STUDENT_GET_WITH_COURSE_NAME = "SELECT * FROM schedule.students WHERE student_id IN"
	    + "(SELECT student_id FROM schedule.students_courses WHERE course_id IN "
	    + "(SELECT course_id FROM schedule.courses WHERE course_name = ?)) ORDER BY student_id";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM schedule.students_courses WHERE student_id = ? and course_id = ?";
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO schedule.students_courses(student_id, course_id) VALUES (?, ?)";
    private static final String MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE = "Can't delete student %d from course %d";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get students";
    private static final String MESSAGE_EXCEPTION_ADD_STUDENT_COURSE = "Can't add pair student-course";

    private ConnectionProvider connectionProvider;
    private StudentDaoImpl studentDaoImpl;
    private CourseDaoImpl courseDaoImpl;
    private SqlScriptRunner sqlScriptRunner;
    private Student studentTestFirst = Student.builder()
	    .withStudentId(1)
	    .withGroupId(1)
	    .withFirstName(TEST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();

    @BeforeEach
    void generateTestData() throws DaosException, SQLException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider(FILEPATH_DB_PROPERTIES);
	studentDaoImpl = new StudentDaoImpl(connectionProvider);
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);
	courseDaoImpl = new CourseDaoImpl(connectionProvider);

	sqlScriptRunner.runSqlScript(PATH_TO_CREATE_SCHEMA_TEST);
    }

    @Test
    void saveShouldBeSaveStudentOnDataBase() throws DaosException {
	Student expected = Student.builder()
		.withGroupId(1)
		.withFirstName(TEST_NAME)
		.withLastName(TEST_LAST_NAME)
		.build();
	studentDaoImpl.add(expected);

	assertEquals(expected.getFirstName(), studentDaoImpl.findById(1).get().getFirstName());
    }

    @Test
    void givenStudents_whenGetStudentsWithCourseMath_thenReturnStudent() throws DaosException {
	List<Student> students = new ArrayList<>();
	students.add(studentTestFirst);

	assertEquals(students, studentDaoImpl.getStudentsWithCourseName("math"));
    }

    @Test
    void givenStudents_whenRemoveStudentId2FromCourseId2_thenRemoveRecord() throws DaosException {
	int studentId = 2;

	studentDaoImpl.removeStudentFromCourse(studentId, 2);
	List<Student> students = studentDaoImpl.getStudentsWithCourseName("math");
	Student actualStudent = students.stream().filter(s -> s.getGroupId() == studentId).findAny().orElse(null);

	assertNull(actualStudent);
    }

    @Test
    void givenStudentWithGroup_whenUpdateStudentWithGroupId_thenReturnNewStudentWithGroupId() throws DaosException {
	List<Student> students = new ArrayList<>();
	students.add(studentTestFirst);
	studentDaoImpl.update(studentTestFirst);

	assertEquals(studentTestFirst.getFirstName(), studentDaoImpl.findById(1).get().getFirstName());
    }

    @Test
    void givenStudentWithGroup_whenAddStudentCourse_thenReturnCourse() throws DaosException {
	Course courseTestFirst = Course.builder()
		.withCourseId(3)
		.withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	List<Student> students = new ArrayList<>();
	courseDaoImpl.add(courseTestFirst);
	students.add(studentTestFirst);
	studentDaoImpl.addStudentCourse(1, 3);

	assertEquals(students, studentDaoImpl.getStudentsWithCourseName("math"));
    }

    @Test
    void shouldReturnDaoExceptionWhengetStudents() throws DaosException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_GET_WITH_COURSE_NAME)).thenThrow(new SQLException());

	StudentDaoImpl studentDaoImplMocked = new StudentDaoImpl(connectionProviderMocked);
	Exception exception = assertThrows(DaosException.class,
		() -> studentDaoImplMocked.getStudentsWithCourseName("math"));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionWhenRemoveStudents() throws DaosException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_COURSE_DELETE)).thenThrow(new SQLException());

	StudentDaoImpl studentDaoImplMocked = new StudentDaoImpl(connectionProviderMocked);
	String expectedMessage = String.format(MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE, 1, 2);
	Exception exception = assertThrows(DaosException.class,
		() -> studentDaoImplMocked.removeStudentFromCourse(1, 2));

	assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionWhenAddStudentToCourse() throws DaosException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_COURSE_ADD)).thenThrow(new SQLException());

	StudentDaoImpl studentDaoImplMocked = new StudentDaoImpl(connectionProviderMocked);
	Exception exception = assertThrows(DaosException.class, () -> studentDaoImplMocked.addStudentCourse(1, 2));

	assertEquals(MESSAGE_EXCEPTION_ADD_STUDENT_COURSE, exception.getMessage());
    }
}
