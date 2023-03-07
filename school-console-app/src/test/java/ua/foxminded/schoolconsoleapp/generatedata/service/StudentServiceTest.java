package ua.foxminded.schoolconsoleapp.generatedata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.dao.StudentDAO;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;
import ua.foxminded.schoolconsoleapp.exception.DomainException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    private static final int NUMBER_OF_STUDENTS = 5;
    private static final int DEFAULT_GROUP_ID = 10;
    private static final int TEST_STUDENT_ID = 3;
    private static final int TEST_COURSE_ID = 2;
    private static final String TEST_COURSE_NAME = "CourseName";
    private static final String TEST_FIRST_NAME = "First";
    private static final String TEST_LAST_NAME = "Last";
    private static final String MESSAGE_CREATE_EXCEPTION = "Don't save student %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get students";
    private static final String MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION = "Can't delete student %d from course %d";
    private static final String MESSAGE_DELETE_EXCEPTION = "Can't delete student";
    private static final String MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Can't add student to course";
    private static final String MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Don't save student %d and course %d";
    
    @Mock
    private StudentDAO studentDao;
    
    @Mock
    private Generator<Student> studentGenerator;
    
    @Mock
    private Random random;
    
    @InjectMocks
    private StudentService studentService;
    
    private Student student = Student.builder()
	    .withGroupId(DEFAULT_GROUP_ID)
	    .withFirstName(TEST_FIRST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();
    
    private List<Student> listOfStudents = new ArrayList<>();
    
    @Test
    void shouldReturnTestStudents() {
	studentService.createTestStudents(NUMBER_OF_STUDENTS);
	verify(studentGenerator, times(1)).generate(NUMBER_OF_STUDENTS);
    }
    
    @Test
    void shouldReturnList_whenAddStudent_thenNumberCallsDao() {
        for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {
            listOfStudents.add(student);
        }
        
        when(studentGenerator.generate(NUMBER_OF_STUDENTS)).thenReturn(listOfStudents);
        studentService.createTestStudents(NUMBER_OF_STUDENTS);
        verify(studentDao, times(NUMBER_OF_STUDENTS)).add(student);
    }
    
    @Test
    void giveStudents_whenGetStudentsWithCourseName_thenReturnListStudent() throws DAOException {
        when(studentDao.getStudentsWithCourseName(anyString())).thenReturn(listOfStudents);
        List<Student> actualStudents = studentService.getStudentsWithCourseName(anyString());
        
        assertEquals(listOfStudents, actualStudents);
    }
    
    @Test
    void  givenDaoExceprion_whenGetStudentsWithCourseName_thenThrowDomainException() throws DAOException {
        when(studentDao.getStudentsWithCourseName(TEST_COURSE_NAME)).thenThrow(DAOException.class);
        Exception exception = assertThrows(DomainException.class, () -> studentService.getStudentsWithCourseName(TEST_COURSE_NAME));
        
        assertEquals(MESSAGE_GET_EXCEPTION, exception.getMessage());
    }
    
    @Test
    void givenCreationOfStudent_whenAddStudent_thenCallStudentDaoAddMethodWithRightArgument() {
        studentService.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
        verify(studentDao, times(1)).add(student);
    }
    
    @Test
    void  givenDaoExceprion_whenCreateStudent_thenThrowDomainException() {
        doThrow(DomainException.class).when(studentDao).add(student);
        Exception exception = assertThrows(DomainException.class, () -> studentService.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME));
        String expectedMessage = String.format(MESSAGE_CREATE_EXCEPTION, student);
       
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    void shouldDeleteStudentFromBaseById() {
	student = Student.builder()
		.withStudentId(TEST_STUDENT_ID)
		.build();
		
	studentService.deleteById(TEST_STUDENT_ID);
	
	verify(studentDao, times(1)).deleteById(TEST_STUDENT_ID);
    }
    
    @Test 
    void shouldReturnDomainExceptionWhenDeleteStudentById() {
	doThrow(new DomainException()).when(studentDao).deleteById(any());
	DomainException exception = assertThrows(DomainException.class, () -> studentService.deleteById(TEST_STUDENT_ID));
	
	assertEquals(MESSAGE_DELETE_EXCEPTION, exception.getMessage());
	verify(studentDao, times(1)).deleteById(TEST_STUDENT_ID);
    }
    
    @Test
    void givenAddStudentToCourseTest() throws DAOException {
	studentService.addStudentToCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
	verify(studentDao, times(1)).addStudentCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
    }
    
    @Test 
    void shouldReturnDomainExceptionWhenAddStudentToCourse() throws DAOException {
	doThrow(DAOException.class).when(studentDao).addStudentCourse(anyInt(), anyInt());
	DomainException exception = assertThrows(DomainException.class, () -> studentService.addStudentToCourse(TEST_STUDENT_ID, TEST_COURSE_ID));
	
	assertEquals(MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, exception.getMessage());
	verify(studentDao, times(1)).addStudentCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
    }
    
    @Test
    void shouldRemoveStudentFromCourseTest() throws DAOException {
	studentService.removeStudentFromCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
	verify(studentDao, times(1)).removeStudentFromCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
    }
    
    @Test
    void shouldReturnDomainExceptionWhenRemoveStudentFromCourse() throws DAOException {
	doThrow(DAOException.class).when(studentDao).removeStudentFromCourse(anyInt(), anyInt());
	DomainException exception = assertThrows(DomainException.class, () -> studentService.removeStudentFromCourse(TEST_STUDENT_ID, TEST_COURSE_ID));
	String expectedMessage = String.format(MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION, TEST_STUDENT_ID, TEST_COURSE_ID);
	
	assertEquals(expectedMessage, exception.getMessage());
	verify(studentDao, times(1)).removeStudentFromCourse(TEST_STUDENT_ID, TEST_COURSE_ID);
    }
    
    @Test
    void shouldReturnAddTestStudentsToCourses() throws DAOException {
	studentService.createTestStudents(NUMBER_OF_STUDENTS);
	for(int i = 0; i < NUMBER_OF_STUDENTS; i++) {
	    studentService.createTestStudentsCourses(i);
	}
	
	verify(studentGenerator, times(1)).generate(NUMBER_OF_STUDENTS);
	verify(studentDao, times(10)).addStudentCourse(anyInt(), anyInt());
    }
    
    @Test
    void shouldReturnDomainExceptionWhenAddTestStudentsToCourses() throws DAOException {
	doThrow(DAOException.class).when(studentDao).addStudentCourse(anyInt(), anyInt());
	DomainException exception = assertThrows(DomainException.class, () -> studentService.createTestStudentsCourses(NUMBER_OF_STUDENTS));
	String expectedMessage = String.format(MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, 1, 1);
	
	assertEquals(expectedMessage, exception.getMessage());
	verify(studentDao, times(1)).addStudentCourse(anyInt(), anyInt());
    }
    
}
