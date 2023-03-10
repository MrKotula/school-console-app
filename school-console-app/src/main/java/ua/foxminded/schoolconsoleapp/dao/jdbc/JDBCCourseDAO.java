package ua.foxminded.schoolconsoleapp.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.CourseDAO;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

public class JDBCCourseDAO extends AbstractJdbcDAO<Course> implements CourseDAO {

    private static final String PROPERTY_COURSE_ADD = "INSERT INTO schedule.courses(course_name, course_description) VALUES (?, ?)";
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT course_id, course_name, course_description FROM schedule.courses WHERE course_id = ?";
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT * FROM schedule.courses limit ? offset ?";
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE schedule.courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String PROPERTY_COURSE_DELETE = "DELETE FROM schedule.courses WHERE course_id = ?";
    private static final String PROPERTY_COURSE_GET_FOR_STUDENT = "SELECT courses.course_id, courses.course_name, courses.course_description "
	    + "FROM schedule.courses " + "INNER JOIN students_courses ON courses.course_id = students_courses.course_id "
	    + "WHERE students_courses.student_id = ?";
    private static final String PROPERTY_COURSE_GET_MISS_FOR_STUDENT = "SELECT course_id, course_name, course_description "
	    + "FROM schedule.courses c " + "WHERE NOT EXISTS (SELECT * FROM students_courses s_c WHERE student_id = ? "
	    + "AND c.course_id = s_c.course_id)";
    
    private static final String FIELD_COURSE_ID = "course_id";
    private static final String FIELD_COURSE_NAME = "course_name";
    private static final String FIELD_COURSE_DESCRIPTION = "course_description";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get courses";
    
    private Course course;
    
    public JDBCCourseDAO(ConnectionProvider connector) {
	super(connector, PROPERTY_COURSE_ADD, PROPERTY_COURSE_GET_BY_ID, PROPERTY_COURSE_GET_ALL, PROPERTY_COURSE_UPDATE, PROPERTY_COURSE_DELETE);
    }

    @Override
    public List<Course> getCoursesForStudentId(int studentId) throws DAOException {
	List<Course> courses = new ArrayList<>();
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(PROPERTY_COURSE_GET_FOR_STUDENT)) {
	    statement.setInt(1, studentId);
	    try (ResultSet resultSet = statement.executeQuery()) {
		
		while (resultSet.next()) {
		    course = Course.builder()
		    .withCourseId(resultSet.getInt(FIELD_COURSE_ID))
		    .withCourseName(resultSet.getString(FIELD_COURSE_NAME))
		    .withCourseDescription(resultSet.getString(FIELD_COURSE_DESCRIPTION))
		    .build();

		    courses.add(course);
		}
	    }
	} catch (SQLException e) {
	    throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
	}
	return courses;
    }

    @Override
    public List<Course> getCoursesMissingForStudentId(int studentId) throws DAOException {
	List<Course> courses = new ArrayList<>();
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(PROPERTY_COURSE_GET_MISS_FOR_STUDENT)) {
	    statement.setInt(1, studentId);
	    try (ResultSet resultSet = statement.executeQuery()) {
		
		while (resultSet.next()) {
		    course = Course.builder()
			    .withCourseId(resultSet.getInt(FIELD_COURSE_ID))
			    .withCourseName(resultSet.getString(FIELD_COURSE_NAME))
			    .withCourseDescription(resultSet.getString(FIELD_COURSE_DESCRIPTION))
			    .build();
		    
		    courses.add(course);
		}
	    }
	} catch (SQLException e) {
	    throw new DAOException(MESSAGE_EXCEPTION_GET_ALL, e);
	}
	return courses;
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
	course = Course.builder()
		.withCourseName(resultSet.getString(FIELD_COURSE_NAME))
		.withCourseDescription(resultSet.getString(FIELD_COURSE_DESCRIPTION))
		.build();
	
	return course;
    }

    @Override
    public void insertSave(PreparedStatement statement, Course entity) throws SQLException {
	statement.setString(1, entity.getCourseName());
	statement.setString(2, entity.getCourseDescription());
    }

    @Override
    public void insertUpdate(PreparedStatement statement, Course entity) throws SQLException {
	statement.setString(1, entity.getCourseName());
	statement.setString(2, entity.getCourseDescription());
	statement.setInt(3, entity.getCourseId());
    }
}
