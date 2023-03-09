package ua.foxminded.schoolconsoleapp.dao.jdbc;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.SqlScriptRunner;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

class JDBCCourseDAOTest {
    private static final String TEST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_SECOND_NAME = "Jane";
    private static final String TEST_SECOND_LAST_NAME = "Does";
    
    private static final String PATH_TO_CREATE_SCHEMA_TEST = "test_schema.sql";
    private static final String PATH_TO_DELETE_SCHEMA_TEST = "drop_all_tables.sql";
    
    private static final String PROPERTY_COURSE_GET_MISS_FOR_STUDENT = "SELECT course_id, course_name, course_description "
	        + "FROM schedule.courses c " + "WHERE NOT EXISTS (SELECT * FROM students_courses s_c WHERE student_id = ? "
	        + "AND c.course_id = s_c.course_id)";
    private static final String PROPERTY_COURSE_GET_FOR_STUDENT = "SELECT courses.course_id, courses.course_name, courses.course_description "
	        + "FROM schedule.courses " + "INNER JOIN students_courses ON courses.course_id = students_courses.course_id "
	        + "WHERE students_courses.student_id = ?";
    
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get courses";
    
    private ConnectionProvider connectionProvider;
    private JDBCCourseDAO jdbcCourseDAO;
    private SqlScriptRunner sqlScriptRunner;
    private JDBCStudentDAO jdbcStudentDAO;
    
    private Student testStudent = Student.builder()
	    .withStudentId(1)
	    .withGroupId(1)
	    .withFirstName(TEST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();
    
    private Student testSecondStudent = Student.builder()
	    .withStudentId(2)
	    .withGroupId(3)
	    .withFirstName(TEST_SECOND_NAME)
	    .withLastName(TEST_SECOND_LAST_NAME)
	    .build();
    
    private Course testCourseMath = Course.builder()
	    .withCourseName("math")
	    .withCourseDescription("course of Mathematics")
	    .build();
    
    @BeforeEach
    void generateTestData() throws DAOException, SQLException, ClassNotFoundException {
	connectionProvider = new ConnectionProvider();
	jdbcCourseDAO = new JDBCCourseDAO(connectionProvider);
	jdbcStudentDAO = new JDBCStudentDAO(connectionProvider);
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);
	
        sqlScriptRunner.runSqlScript(PATH_TO_CREATE_SCHEMA_TEST);
        
        jdbcStudentDAO.add(testStudent);
    }

    @AfterEach
    void cleanTestData() throws DAOException, SQLException {
	Connection connection = connectionProvider.getConnection();
	sqlScriptRunner = new SqlScriptRunner(connection);

	sqlScriptRunner.runSqlScript(PATH_TO_DELETE_SCHEMA_TEST);
    }

    @Test
    void saveShouldBeAddNewEntityIntoDataBase() throws SQLException, DAOException {
	Course expected = testCourseMath;
	jdbcCourseDAO.add(expected);
	Course actual = jdbcCourseDAO.findById(3).get();

	assertEquals(expected, actual);
    }

    @Test
    void updateEntityShouldBeUpdateDateOfCourseIntoDataBase() throws DAOException {
	Course expected = Course.builder()
		.withCourseId(5)
		.withCourseName("math")
		.withCourseDescription("course of Mathematics")
		.build();
	
	jdbcCourseDAO.update(expected);

	assertEquals(expected.getCourseName(), jdbcCourseDAO.findById(1).get().getCourseName());
    }

    @Test
    void findAllEntityShouldBeReturnEntityFromDataBase() {
	List<Course> expected = Arrays.asList(testCourseMath);
	
	assertEquals(expected, jdbcCourseDAO.findAll(0, 1));
    }

    @Test
    void findByIdShouldBeReturnEntityFromDataBase() throws DAOException {
	Course expected = testCourseMath;
	Course actual = jdbcCourseDAO.findById(1).get();

	assertEquals(expected, actual);
    }

    @Test
    void deleteEntityShouldBeDeleteCourseFromDataBase() throws DAOException {
	jdbcCourseDAO.deleteById(9);
	Optional<Course> expected = Optional.empty();

	assertEquals(expected, jdbcCourseDAO.findById(9));
    }
    
    @Test
    void getCoursesMissingForStudentIdTest() throws DAOException {
	Course testCourseMath = Course.builder()
		    .withCourseId(1)
		    .withCourseName("math")
		    .withCourseDescription("course of Mathematics")
		    .build();
	    
	Course testCourseBiology = Course.builder()
		    .withCourseId(2)
		    .withCourseName("biology")
		    .withCourseDescription("course of Biology")
		    .build();
	    
	Course testCourseChemistry = Course.builder()
		    .withCourseId(3)
		    .withCourseName("chemistry")
		    .withCourseDescription("course of Chemistry")
		    .build();
	
	List<Course> expected = Arrays.asList(testCourseMath,
		testCourseBiology, testCourseChemistry);
	jdbcCourseDAO.add(testCourseChemistry);
	List<Course> actual = jdbcCourseDAO.getCoursesMissingForStudentId(1);

	assertEquals(expected, actual);
    }
    
    @Test
    void getCoursesForStudentIdTest() throws DAOException {
	 List<Course> courses = new ArrayList<>();
	 Course testCourse = Course.builder()
		    .withCourseId(1)
		    .withCourseName("math")
		    .withCourseDescription("course of Mathematics")
		    .build();
	 
         courses.add(testCourse);
         jdbcStudentDAO.addStudentCourse(5, 1);
         
         assertEquals(courses, jdbcCourseDAO.getCoursesForStudentId(5));
         
         jdbcStudentDAO.removeStudentFromCourse(5, 1);
    }
    
    @Test
    void givenStudents_whenGetStudentsWithCourseMath_thenReturnStudent() throws DAOException {
	List<Student> students = new ArrayList<>();
	students.add(testStudent);
	students.add(testSecondStudent);
	jdbcStudentDAO.addStudentCourse(1, 1);
	jdbcStudentDAO.addStudentCourse(2, 1);
	
	assertEquals(students, jdbcStudentDAO.getStudentsWithCourseName("math"));
	
	jdbcStudentDAO.removeStudentFromCourse(1, 1);
	jdbcStudentDAO.removeStudentFromCourse(2, 1);
    }
    
    @Test
    void shouldReturnDaoExceptionWhenGetCourses() throws DAOException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_COURSE_GET_MISS_FOR_STUDENT)).thenThrow(new SQLException());
	JDBCCourseDAO jdbcCourseDAOMocked = new JDBCCourseDAO(connectionProviderMocked);

	Exception exception = assertThrows(DAOException.class,() -> jdbcCourseDAOMocked.getCoursesMissingForStudentId(1));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }
    
    @Test
    void shouldReturnDaoExceptionWhenGetCoursesByStudentId() throws DAOException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_COURSE_GET_FOR_STUDENT)).thenThrow(new SQLException());
	JDBCCourseDAO jdbcCourseDAOMocked = new JDBCCourseDAO(connectionProviderMocked);

	Exception exception = assertThrows(DAOException.class,() -> jdbcCourseDAOMocked.getCoursesForStudentId(1));

	assertEquals(MESSAGE_EXCEPTION_GET_ALL, exception.getMessage());
    }
}
