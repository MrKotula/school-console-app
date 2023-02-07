package ua.foxminded.schoolconsoleapp.generatedata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.dao.CourseDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCCourseDAO;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DAOException;
import ua.foxminded.schoolconsoleapp.exception.DomainException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    private static final int NUMBER_OF_COURSES = 5;
    private static final int STUDENT_ID_EXCEPTION = 6;
    private static final String EXCEPTION_MESSAGE = "Can't get courses";
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save course %s in base";
    
    @Mock
    private CourseDAO courseDao;
    
    @Mock
    private Generator<Course> courseGenerator;
    
    @InjectMocks
    private CourseService courseService = new CourseService(courseDao, courseGenerator);
    
    @Mock
    private JDBCCourseDAO jdbcCourseDAO;
    
    private List<Course> listOfCourses = new ArrayList<>();
    private Course course = new Course("math", "course of math");
    
    @Test
    void shouldReturnTestCourses() {
	courseService.createTestCourses(NUMBER_OF_COURSES);
	verify(courseGenerator, times(1)).generate(NUMBER_OF_COURSES);
    }
    
    @Test
    void givenAddedCourses_whenGenerateNumberCourses_thenCourseDaoIsCalledAsMuchTimeAsNumberCourses() {
	for (int i = 0; i < NUMBER_OF_COURSES; i++) {
            listOfCourses.add(course);
        }
	
        when(courseGenerator.generate(NUMBER_OF_COURSES)).thenReturn(listOfCourses);
        courseService.createTestCourses(NUMBER_OF_COURSES);
        verify(courseGenerator, times(1)).generate(NUMBER_OF_COURSES);
    }
    
    @Test
    void shouldReturnExceptionIfMissingOfStudents( ) {
    when(courseService.getCoursesMissingForStudent(STUDENT_ID_EXCEPTION)).thenThrow(DAOException.class);
    Exception exception = assertThrows(DomainException.class, () -> courseService.getCoursesMissingForStudent(STUDENT_ID_EXCEPTION));
    
    assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }
    
    @Test
    void shouldReturnExceptionIfgetCoursesOfStudents( ) {
    when(courseService.getCoursesForStudent(STUDENT_ID_EXCEPTION)).thenThrow(DAOException.class);
    Exception exception = assertThrows(DomainException.class, () -> courseService.getCoursesForStudent(STUDENT_ID_EXCEPTION));
    
    assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }
    
    @Test
    void shouldReturnExceptionIfCantAddCoursesToBase( ) {
	doThrow(DomainException.class).when(courseDao).add(course);
	RuntimeException exception = assertThrows(DomainException.class, () -> courseService.addCourseToBase(course));
	String expectedMessage = String.format(MASK_MESSAGE_ADD_EXCEPTION, course);
	verify(courseDao, times(1)).add(any());
	assertEquals(expectedMessage, exception.getMessage());
    }
}
