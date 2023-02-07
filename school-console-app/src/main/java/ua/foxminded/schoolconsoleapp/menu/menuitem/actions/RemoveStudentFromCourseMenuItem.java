package ua.foxminded.schoolconsoleapp.menu.menuitem.actions;

import java.util.List;
import java.util.Scanner;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;

public class RemoveStudentFromCourseMenuItem extends MenuItem {
    private static final String MESSAGE_INPUT_COURSE_ID = "Input course_id from list courses for removing student: ";
    private static final String MESSAGE_INPUT_STUDENT_ID = "Input student_id for remove from course: ";
    private static final String MASK_MESSAGE_REMOVE_STUDENT_COURSE = "Student %d deleted from course %d";

    private CourseService courseService;
    private StudentService studentService;
    private Scanner scanner;

    public RemoveStudentFromCourseMenuItem(String name, CourseService courseService,
	    StudentService studentService, Scanner scanner) {
	super(name);
	this.courseService = courseService;
	this.studentService = studentService;
	this.scanner = scanner;
    }

    @Override
    public void execute() {
	System.out.print(MESSAGE_INPUT_STUDENT_ID);
	int studentId = Integer.parseInt(scanner.nextLine());
	List<Course> courses = courseService.getCoursesForStudent(studentId);
	courses.stream().forEach(System.out::println);

	System.out.print(MESSAGE_INPUT_COURSE_ID);
	int courseId = Integer.parseInt(scanner.nextLine());
	studentService.removeStudentFromCourse(studentId, courseId);
	System.out.println(String.format(MASK_MESSAGE_REMOVE_STUDENT_COURSE, studentId, courseId));
    }
}
