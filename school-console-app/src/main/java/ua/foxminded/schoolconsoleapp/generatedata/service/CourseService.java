package ua.foxminded.schoolconsoleapp.generatedata.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.schoolconsoleapp.dao.CoursesDao;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

public class CourseService {
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save course %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get courses";
    
    private static Logger logger = LoggerFactory.getLogger(CourseService.class);
    private CoursesDao coursesDao;
    private Generator<Course> generator;

    public CourseService(CoursesDao coursesDao, Generator<Course> generator) {
	this.coursesDao = coursesDao;
	this.generator = generator;
    }

    public void createTestCourses(int numberCourses) {
	List<Course> courses = generator.generate(numberCourses);
	courses.forEach(t -> {
	    try {
		addCourseToBase(t);
	    } catch (DaosException e) {
		 logger.info(e.getMessage());
	    }
	});
    }

    public List<Course> getCoursesMissingForStudent(int studentId) throws DaosException {
	try {
	    return coursesDao.getCoursesMissingForStudentId(studentId);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_GET_EXCEPTION, e);
	}
    }

    public List<Course> getCoursesForStudent(int studentId) throws DaosException {
	try {
	    return coursesDao.getCoursesForStudentId(studentId);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_GET_EXCEPTION, e);
	}
    }

    public void addCourseToBase(Course course) throws DaosException {
	try {
	    coursesDao.add(course);
	} catch (DaosException e) {
	    throw new DaosException(String.format(MASK_MESSAGE_ADD_EXCEPTION, course), e);
	}
    }
}
