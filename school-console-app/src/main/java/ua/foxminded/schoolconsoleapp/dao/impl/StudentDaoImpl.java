package ua.foxminded.schoolconsoleapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.StudentsDao;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public class StudentDaoImpl extends AbstractDaoImpl<Student> implements StudentsDao {
    private static final String PROPERTY_STUDENT_ADD = "INSERT INTO schedule.students(group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT student_id, group_id, first_name, last_name FROM schedule.students WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT * FROM schedule.students limit ? offset ?";
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE schedule.students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM schedule.students WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_GET_WITH_COURSE_NAME = "SELECT * FROM schedule.students WHERE student_id IN"
	    + "(SELECT student_id FROM schedule.students_courses WHERE course_id IN "
	    + "(SELECT course_id FROM schedule.courses WHERE course_name = ?)) ORDER BY student_id";
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO schedule.students_courses(student_id, course_id) VALUES (?, ?)";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM schedule.students_courses WHERE student_id = ? and course_id = ?";

    private static final String FIELD_STUDENT_ID = "student_id";
    private static final String FIELD_GROUP_ID = "group_id";
    private static final String FIELD_FIRST_NAME = "first_name";
    private static final String FIELD_LAST_NAME = "last_name";

    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get students";
    private static final String MESSAGE_EXCEPTION_ADD_STUDENT_COURSE = "Can't add pair student-course";
    private static final String MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE = "Can't delete student %d from course %d";

    public StudentDaoImpl(ConnectionProvider connectionProvider) {
	super(connectionProvider, PROPERTY_STUDENT_ADD, PROPERTY_STUDENT_GET_BY_ID, PROPERTY_STUDENT_GET_ALL,
		PROPERTY_STUDENT_UPDATE, PROPERTY_STUDENT_DELETE);
    }

    @Override
    public void addStudentCourse(int studentId, int courseId) throws DaosException {
	try (Connection connection = connectionProvider.getConnection()) {
	    try (PreparedStatement statement = connection.prepareStatement(PROPERTY_STUDENT_COURSE_ADD)) {
		statement.setInt(1, studentId);
		statement.setInt(2, courseId);
		statement.executeUpdate();
	    }
	} catch (SQLException e) {
	    throw new DaosException(MESSAGE_EXCEPTION_ADD_STUDENT_COURSE, e);
	}
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws DaosException {
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(PROPERTY_STUDENT_COURSE_DELETE)) {
	    statement.setInt(1, studentId);
	    statement.setInt(2, courseId);
	    statement.executeUpdate();
	} catch (SQLException e) {
	    throw new DaosException(String.format(MESSAGE_EXCEPTION_DELETE_STUDENT_COURSE, studentId, courseId), e);
	}
    }

    @Override
    public List<Student> getStudentsWithCourseName(String courseName) throws DaosException {
	List<Student> students = new ArrayList<>();
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(PROPERTY_STUDENT_GET_WITH_COURSE_NAME)) {
	    statement.setString(1, courseName);
	    try (ResultSet resultSet = statement.executeQuery()) {
		while (resultSet.next()) {
		    Student student = Student.builder().withStudentId(resultSet.getInt(FIELD_STUDENT_ID))
			    .withGroupId(resultSet.getInt(FIELD_GROUP_ID))
			    .withFirstName(resultSet.getString(FIELD_FIRST_NAME))
			    .withLastName(resultSet.getString(FIELD_LAST_NAME)).build();

		    students.add(student);
		}
	    }
	} catch (SQLException e) {
	    throw new DaosException(MESSAGE_EXCEPTION_GET_ALL, e);
	}
	return students;
    }

    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
	
	return Student.builder()
		.withFirstName(resultSet.getString(FIELD_FIRST_NAME))
		.withLastName(resultSet.getString(FIELD_LAST_NAME))
		.build();
    }

    @Override
    protected void insertSave(PreparedStatement statement, Student entity) throws SQLException {
	statement.setInt(1, entity.getGroupId());
	statement.setString(2, entity.getFirstName());
	statement.setString(3, entity.getLastName());
    }

    @Override
    protected void insertUpdate(PreparedStatement statement, Student entity) throws SQLException {
	statement.setInt(1, entity.getGroupId());
	statement.setString(2, entity.getFirstName());
	statement.setString(3, entity.getLastName());
	statement.setInt(4, entity.getStudentId());
    }
}
