package ua.foxminded.schoolconsoleapp.menu.menuitem.actions;

import java.util.List;
import java.util.Scanner;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;

public class GetStudentsWithCourseNameMenuItem extends MenuItem {
    private static final String MESSAGE_INPUT_COURSE_NAME = "Input course name: ";

    private StudentService studentService;
    private Scanner scanner;

    public GetStudentsWithCourseNameMenuItem(String name, StudentService studentService, Scanner scanner) {
	super(name);
	this.studentService = studentService;
	this.scanner = scanner;
    }

    @Override
    public void execute() {
	System.out.print(MESSAGE_INPUT_COURSE_NAME);
	String courseName = scanner.nextLine();
	List<Student> students = studentService.getStudentsWithCourseName(courseName);
	students.stream().forEach(System.out::println);
    }
}
