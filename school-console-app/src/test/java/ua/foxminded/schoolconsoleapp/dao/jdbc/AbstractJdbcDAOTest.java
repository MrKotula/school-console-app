package ua.foxminded.schoolconsoleapp.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;
import ua.foxminded.schoolconsoleapp.exception.DomainException;

class AbstractJdbcDAOTest {
    private static final String TEST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    
    private static final String PROPERTY_STUDENT_ADD = "INSERT INTO schedule.students(group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM schedule.students WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE schedule.students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT * FROM schedule.students limit ? offset ?";
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT student_id, group_id, first_name, last_name FROM schedule.students WHERE student_id = ?";

    private static final String RESULT_OF_UPDATE_EXCEPTION = "Update is failed!";
    private static final String RESULT_OF_INSERT_EXCEPTION = "Insertion is failed!";
    
    private Student testStudent = Student.builder()
	    .withGroupId(1)
	    .withFirstName(TEST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();
	    
    @Test
    void shouldThrowDomainExceptionWhenUseAddTest() throws DomainException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_ADD)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	DomainException exception = assertThrows(DomainException.class, () -> jdbcStudentDAOMocked.add(testStudent));

	assertEquals(RESULT_OF_INSERT_EXCEPTION, exception.getMessage());
	verify(connectionMocked, times(1)).prepareStatement(PROPERTY_STUDENT_ADD);  
    }
    
    @Test
    void shouldThrowDomainExceptionWhenUseDeleteTest() throws DomainException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_DELETE)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	DomainException exception = assertThrows(DomainException.class, () -> jdbcStudentDAOMocked.deleteById(1));

	assertEquals(RESULT_OF_UPDATE_EXCEPTION, exception.getMessage());
	verify(connectionMocked, times(1)).prepareStatement(PROPERTY_STUDENT_DELETE);  
    }
    
    @Test
    void shouldThrowDomainExceptionWhenUseUpdateTest() throws DomainException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_UPDATE)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	DomainException exception = assertThrows(DomainException.class, () -> jdbcStudentDAOMocked.update(testStudent));

	assertEquals(RESULT_OF_UPDATE_EXCEPTION, exception.getMessage());
	verify(connectionMocked, times(1)).prepareStatement(PROPERTY_STUDENT_UPDATE);  
    }
    
    @Test
    void shouldThrowDomainExceptionWhenUseFindAllTest() throws DomainException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_GET_ALL)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	DomainException exception = assertThrows(DomainException.class, () -> jdbcStudentDAOMocked.findAll(1, 1));

	assertEquals(RESULT_OF_UPDATE_EXCEPTION, exception.getMessage());
	verify(connectionMocked, times(1)).prepareStatement(PROPERTY_STUDENT_GET_ALL);  
    }
    
    @Test
    void shouldThrowDomainExceptionWhenUseFindByIdTest() throws DomainException, SQLException {
	ConnectionProvider connectionProviderMocked = mock(ConnectionProvider.class);
	Connection connectionMocked = mock(Connection.class);

	when(connectionProviderMocked.getConnection()).thenReturn(connectionMocked);
	when(connectionMocked.prepareStatement(PROPERTY_STUDENT_GET_BY_ID)).thenThrow(new SQLException());
	JDBCStudentDAO jdbcStudentDAOMocked = new JDBCStudentDAO(connectionProviderMocked);

	assertThrows(DAOException.class, () -> jdbcStudentDAOMocked.findById(1));
	verify(connectionMocked, times(1)).prepareStatement(PROPERTY_STUDENT_GET_BY_ID);  
    }
}
