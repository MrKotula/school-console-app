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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ua.foxminded.schoolconsoleapp.dao.CoursesDao;
import ua.foxminded.schoolconsoleapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;
import ua.foxminded.schoolconsoleapp.tool.BusinessWorker;
import ua.foxminded.schoolconsoleapp.tool.MemoryAppender;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    private static final String TEST_COURSE_NAME = "math";
    private static final String TEST_COURSE_DISCRIPTION = "math";
    private static final int NUMBER_OF_COURSES = 5;
    private static final int STUDENT_ID_EXCEPTION = 6;
    private static final String EXCEPTION_MESSAGE = "Can't get courses";
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save course %s in base";

    @Mock
    private CoursesDao coursesDao;

    @Mock
    private Generator<Course> courseGenerator;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseDaoImpl jdbcCourseDAO;

    private List<Course> listOfCourses = new ArrayList<>();
    private Course course = Course.builder()
	    .withCourseName(TEST_COURSE_NAME)
	    .withCourseDescription(TEST_COURSE_DISCRIPTION)
	    .build();
    private MemoryAppender memoryAppender;
    private Logger logger;
    
    @BeforeEach
    public void setup() {
        logger = (Logger) LoggerFactory.getLogger(CourseService.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void shouldReturnTestCourses() throws DaosException {
	courseService.createTestCourses(NUMBER_OF_COURSES);
	verify(courseGenerator, times(1)).generate(NUMBER_OF_COURSES);
    }

    @Test
    void givenAddedCourses_whenGenerateNumberCourses_thenCourseDaoIsCalledAsMuchTimeAsNumberCourses() throws DaosException {
	for (int i = 0; i < NUMBER_OF_COURSES; i++) {
	    listOfCourses.add(course);
	}
	when(courseGenerator.generate(NUMBER_OF_COURSES)).thenReturn(listOfCourses);
	courseService.createTestCourses(NUMBER_OF_COURSES);

	verify(courseGenerator, times(1)).generate(NUMBER_OF_COURSES);
    }

    @Test
    void shouldReturnDaoExceptionIfMissingOfStudents( ) throws DaosException {
	when(courseService.getCoursesMissingForStudent(STUDENT_ID_EXCEPTION)).thenThrow(DaosException.class);
	Exception exception = assertThrows(DaosException.class, () -> courseService.getCoursesMissingForStudent(STUDENT_ID_EXCEPTION));
    
	assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionIfgetCoursesOfStudents( ) throws DaosException {
	when(courseService.getCoursesForStudent(STUDENT_ID_EXCEPTION)).thenThrow(DaosException.class);
	Exception exception = assertThrows(DaosException.class, () -> courseService.getCoursesForStudent(STUDENT_ID_EXCEPTION));
    
	assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldReturnDaoExceptionIfCantAddCoursesToBase() throws DaosException {
	courseService = new CourseService(coursesDao, courseGenerator);

	doThrow(DaosException.class).when(coursesDao).add(course);
	DaosException exception = assertThrows(DaosException.class, () -> courseService.addCourseToBase(course));
	String expectedMessage = String.format(MASK_MESSAGE_ADD_EXCEPTION, course);

	verify(coursesDao, times(1)).add(any());
	assertEquals(expectedMessage, exception.getMessage());
    }
}