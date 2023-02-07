package ua.foxminded.schoolconsoleapp.menu;

import java.util.InputMismatchException;
import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.viewprovider.ViewProvider;

public class MenuStarterImpl implements MenuStarter {
    private static final String MESSAGE_FIRST_NAME = "Input first name: ";
    private static final String MESSAGE_LAST_NAME = "Input last name: ";
    private static final String MASK_MESSAGE_ADD_STUDENT = "Student %s %s is created!";
    private static final String MESSAGE_INPUT_COURSE_NAME = "Input course name: ";
    private static final String MESSAGE_INPUT_STUDENT_ID = "Input student_id for add to course: ";
    private static final String MESSAGE_INPUT_COURSE_ID = "Input course_id from list courses for removing student: ";
    private static final String MESSAGE_INPUT_STUDENT_ID_REMOVE = "Input student_id for remove from course: ";
    private static final String MASK_MESSAGE_REMOVE_STUDENT_COURSE = "Student %d deleted from course %d";
    private static final String MASK_MESSAGE_ADD_STUDENT_COURSE = "Student %d added to course %d";
    private static final String MASK_MESSAGE_DELETE_STUDENT = "Student with id %d is deleted";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";
    private static final String MESSAGE_QUIT_APPLICATION = "Quitting application...";

    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ViewProvider viewProvider;

    public MenuStarterImpl(GroupService groupService, StudentService studentService, CourseService courseService,
	    ViewProvider viewProvider) {
	this.groupService = groupService;
	this.studentService = studentService;
	this.courseService = courseService;
	this.viewProvider = viewProvider;
    }

    @Override
    public void startMenu() throws DaosException {
	boolean isWork = true;
	while (isWork) {
	    viewProvider.printMessage("Main menu\n" + "1. Find all groups with less or equals student count\n"
		    + "2. Find all students related to course with given name\n" + "3. Add new student\n"
		    + "4. Delete student by STUDENT_ID\n" + "5. Add a student to the course (from a list)\n"
		    + "6. Remove the student from one of his or her courses\n" + "0. Exit\n"
		    + "Enter a number from the list:\n");
	    int choose = viewProvider.readInt();

	    switch (choose) {
	    case 1:
		findAllGroupsWithStudentCount();
		break;
	    case 2:
		findAllStudentsToCourseName();
		break;
	    case 3:
		addStudent();
		break;
	    case 4:
		deleteStudentById();
		break;
	    case 5:
		addStudentToCourse();
		break;
	    case 6:
		removeStudentFromCourse();
		break;
	    case 0:
		isWork = false;
		viewProvider.printMessage(MESSAGE_QUIT_APPLICATION);
		break;
	    default:
		viewProvider.printMessage("Incorrect command\n");
	    }
	}
    }

    @Override
    public void findAllGroupsWithStudentCount() throws DaosException {
	int studentCount = inputStudentCount();

	List<Group> groups = groupService.getGroupsWithLessEqualsStudentCount(studentCount);
	groups.stream().forEach(System.out::println);
    }

    @Override
    public void findAllStudentsToCourseName() throws DaosException {
	System.out.print(MESSAGE_INPUT_COURSE_NAME);
	String courseName = viewProvider.read();

	List<Student> students = studentService.getStudentsWithCourseName(courseName);
	students.stream().forEach(System.out::println);
    }

    @Override
    public void addStudent() throws DaosException {
	System.out.print(MESSAGE_FIRST_NAME);
	String firstName = viewProvider.read();

	System.out.print(MESSAGE_LAST_NAME);
	String lastName = viewProvider.read();

	studentService.createStudent(firstName, lastName);
	System.out.println(String.format(MASK_MESSAGE_ADD_STUDENT, firstName, lastName));
    }

    @Override
    public void deleteStudentById() throws DaosException {
	int studentId = inputStudentId();

	studentService.deleteById(studentId);
	System.out.println(String.format(MASK_MESSAGE_DELETE_STUDENT, studentId));
    }

    @Override
    public void addStudentToCourse() throws DaosException {
	System.out.print(MESSAGE_INPUT_STUDENT_ID);
	int studentId = viewProvider.readInt();

	List<Course> courses = courseService.getCoursesMissingForStudent(studentId);
	courses.stream().forEach(System.out::println);

	System.out.print("Input course_id from list courses for adding student: ");
	int courseId = viewProvider.readInt();

	studentService.addStudentToCourse(studentId, courseId);
	System.out.println(String.format(MASK_MESSAGE_ADD_STUDENT_COURSE, studentId, courseId));
    }

    @Override
    public void removeStudentFromCourse() throws DaosException {
	System.out.print(MESSAGE_INPUT_STUDENT_ID_REMOVE);
	int studentId = viewProvider.readInt();

	List<Course> courses = courseService.getCoursesForStudent(studentId);
	courses.stream().forEach(System.out::println);

	System.out.print(MESSAGE_INPUT_COURSE_ID);
	int courseId = viewProvider.readInt();

	studentService.removeStudentFromCourse(studentId, courseId);
	System.out.println(String.format(MASK_MESSAGE_REMOVE_STUDENT_COURSE, studentId, courseId));
    }

    private int inputStudentId() throws DaosException {
	int result = -1;

	do {
	    System.out.print("Input student_id for deleting: ");
	    try {
		result = viewProvider.readInt();
	    } catch (InputMismatchException e) {
		throw new DaosException(MESSAGE_EXCEPTION_NOT_NUMBER);
	    }
	} while (result == -1);

	return result;
    }

    private int inputStudentCount() {
	int studentCount = 0;
	System.out.print("Input student count: ");

	while (viewProvider.readBoolean()) {
	    studentCount = viewProvider.readInt();
	    break;
	}

	return studentCount;
    }
}
