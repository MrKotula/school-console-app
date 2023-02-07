package ua.foxminded.schoolconsoleapp.generatedata.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.schoolconsoleapp.dao.StudentsDao;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

public class StudentService {
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save student %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get students";
    private static final String MESSAGE_DELETE_EXCEPTION = "Can't delete student";
    private static final String MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Can't add student to course";
    private static final String MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION = "Don't save student %d and course %d";
    private static final String MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION = "Can't delete student %d from course %d";
    private static final int STUDENT_ID = 0;
    private static final int COURSE_ID = 1;
    private static final int DEFAULT_GROUP_ID = 10;
    
    private static Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentsDao studentsDao;
    private final Generator<Student> generator;

    public StudentService(StudentsDao studentsDao, Generator<Student> generator) {
	this.studentsDao = studentsDao;
	this.generator = generator;
    }

    public void createTestStudents(int numberStudents) {
	List<Student> students = generator.generate(numberStudents);
	students.forEach(test -> {
	    try {
		addStudentToBase(test);
	    } catch (DaosException e) {
		logger.info(e.getMessage());
	    }
	});
    }

    public void createTestStudentsCourses(int numberStudents) {
	List<Integer[]> studentCourses = new ArrayList<>();
	
	for (int i = 0; i < numberStudents; i++) {
	    assignCourses(i + 1, studentCourses);
	}
	addStudentsCoursesToBase(studentCourses);
    }

    public List<Student> getStudentsWithCourseName(String courseName) throws DaosException {
	try {
	    return studentsDao.getStudentsWithCourseName(courseName);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_GET_EXCEPTION, e);
	}
    }

    public void createStudent(String firstName, String lastName) throws DaosException {
	Student student = Student.builder()
		.withFirstName(firstName)
		.withLastName(lastName)
		.withGroupId(DEFAULT_GROUP_ID)
		.build();
		
	addStudentToBase(student);
    }

    public void addStudentToBase(Student student) throws DaosException {
	try {
	    studentsDao.add(student);
	} catch (DaosException e) {
	    throw new DaosException(String.format(MASK_MESSAGE_ADD_EXCEPTION, student), e);
	}
    }

    public void deleteById(int id) throws DaosException {
	try {
	    studentsDao.deleteById(id);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_DELETE_EXCEPTION, e);
	}
    }

    public void addStudentToCourse(int studentId, int courseId) throws DaosException {
	try {
	    studentsDao.addStudentCourse(studentId, courseId);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, e);
	}
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws DaosException {
	try {
	    studentsDao.removeStudentFromCourse(studentId, courseId);
	} catch (DaosException e) {
	    throw new DaosException(String.format(MASK_MESSAGE_DELETE_STUDENT_COURSE_EXCEPTION, studentId, courseId),
		    e);
	}
    }
    
    @SuppressWarnings("java:S5413")
    private void assignCourses(int studentId, List<Integer[]> studentCourses) {
	Random random = new Random();
	int numberCoursesPerStudent = random.nextInt(2) + 1;
	List<Integer> coursesId = new LinkedList<>();
	Collections.addAll(coursesId, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	for (int i = 0; i < numberCoursesPerStudent; i++) {
	    int numberCourse = random.nextInt(coursesId.size());
	    Integer[] studentCourse = { studentId, coursesId.get(numberCourse)};
	    studentCourses.add(studentCourse);
	    coursesId.remove(numberCourse);
	}
    }

    private void addStudentsCoursesToBase(List<Integer[]> studentsCourses) {
	studentsCourses.forEach(pair -> {
	    try {
		studentsDao.addStudentCourse(pair[STUDENT_ID], pair[COURSE_ID]);
	    } catch (DaosException e) {
		logger.info(String.format(MASK_MESSAGE_ADD_STUDENT_COURSE_EXCEPTION, pair[STUDENT_ID], pair[COURSE_ID]));
	    }
	});
    }
}
