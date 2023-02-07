package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.SqlScriptRunner;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

class CourseDaoImplTest {
    private static final String FILEPATH_DB_PROPERTIES = "src/test/resources/db.propertiesTest.properties";

    private static final String TEST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String PATH_TO_CREATE_SCHEMA_TEST = "test_schema.sql";
    private static final String PROPERTY_COURSE_GET_FOR_STUDENT = "SELECT courses.course_id, courses.course_name, courses.course_description "
	    + "FROM schedule.courses "
	    + "INNER JOIN schedule.students_courses ON courses.course_id = students_courses.course_id "
	    + "WHERE students_courses.student_id = ?";
    private static final String PROPERTY_COURSE_GET_MISS_FOR_STUDENT = "SELECT course_id, course_name, course_description "
	    + "FROM schedule.courses c "
	    + "WHERE NOT EXISTS (SELECT * FROM schedule.students_courses s_c WHERE student_id = ? "
	    + "AND c.course_id = s_c.course_id)";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get courses";

    private ConnectionProvider connectionProvider;
    private CourseDaoImpl courseDaoImpl;
    private SqlScriptRunner sqlScriptRunner;
    private StudentDaoImpl studentDaoImpl;
    private GroupDaoImpl groupDaoImpl;

    private Course testCourseMath = Course.builder()
	    .withCourseId(1)
	    .withCourseName("math")
	    .withCourseDescription("course of Mathematics")
	    .build();

    private Course testCourseBiology = Course.builder()
	    .withCourseId(2)
	    .withCourseName("biology")
	    .withCourseDescription("course of Biology")
	    .build();

    private Student testStudent = Student.builder()
	    .withStudentId(1)
	    .withGroupId(1)
	    .withFirstName(TEST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();

    @BeforeEach
    void generateTestData() throws DaosException, SQLException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider(FILEPATH_DB_PROPERTIES);
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);

	courseDaoImpl = new CourseDaoImpl(connectionProvider);
	studentDaoImpl = new StudentDaoImpl(connectionProvider);
	groupDaoImpl = new GroupDaoImpl(connectionProvider);

	sqlScriptRunner.runSqlScript(PATH_TO_CREATE_SCHEMA_TEST);
    }

    @Test
    void saveShouldBeAddNewEntityIntoDataBase() throws SQLException, DaosException {
	List<Course> expected = Arrays.asList(testCourseMath, testCourseBiology);
	courseDaoImpl.add(testCourseMath);
	courseDaoImpl.add(testCourseBiology);
	List<Course> actual = courseDaoImpl.getCoursesForStudentId(1);

	assertEquals(expected, actual);
    }

    @Test
    void updateEntityShouldBeUpdateDateOfCourseIntoDataBase() throws DaosException {
	Course expected = Course.builder()
		.withCourseId(5)
		.withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	courseDaoImpl.update(expected);

	assertEquals(expected.getCourseName(), courseDaoImpl.findById(1).get().getCourseName());
    }

    @Test
    void findAllEntityShouldBeReturnEntityFromDataBase() throws DaosException {
	testCourseMath = Course.builder()
		.withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	List<Course> expected = Arrays.asList(testCourseMath);

	assertEquals(expected, courseDaoImpl.findAll(0, 1));
    }

    @Test
    void findByIdShouldBeReturnEntityFromDataBase() throws DaosException {
	Course expected = testCourseMath = Course.builder()
		.withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	Course actual = courseDaoImpl.findById(1).get();

	assertEquals(expected, actual);
    }

    @Test
    void deleteEntityShouldBeDeleteCourseFromDataBase() throws DaosException {
	courseDaoImpl.deleteById(9);
	Optional<Course> expected = Optional.empty();

	assertEquals(expected, courseDaoImpl.findById(9));
    }

    @Test
    void shouldReturnDaoExceptionWhenGetCoursesMissingForStudentId() throws DaosException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_COURSE_GET_FOR_STUDENT)).thenThrow(new SQLException());

	CourseDaoImpl courseDaoImpl = new CourseDaoImpl(connectionProviderMocked);
	Exception exception = assertThrows(DaosException.class, () -> courseDaoImpl.getCoursesForStudentId(1));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionWhenGetCoursesByStudentId() throws DaosException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);
	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_COURSE_GET_MISS_FOR_STUDENT)).thenThrow(new SQLException());

	CourseDaoImpl courseDaoImpl = new CourseDaoImpl(connectionProviderMocked);
	Exception exception = assertThrows(DaosException.class, () -> courseDaoImpl.getCoursesMissingForStudentId(1));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }

    @Test
    void givenStudents_whenGetStudentsWithCourseMath_thenReturnStudent() throws DaosException {
	List<Student> students = new ArrayList<>();
	students.add(testStudent);

	assertEquals(students, studentDaoImpl.getStudentsWithCourseName("math"));
    }

    @Test
    void givenStudent_whenGetStudentsWithId_thenReturnStudent() throws DaosException {
	Course courseTestFirst = Course.builder()
		.withCourseId(3).withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	List<Course> course = new ArrayList<>();
	course.add(courseTestFirst);
	courseDaoImpl.add(courseTestFirst);

	assertEquals(course, courseDaoImpl.getCoursesMissingForStudentId(1));
    }
}
