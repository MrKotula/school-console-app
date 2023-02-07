package ua.foxminded.schoolconsoleapp.menu.menuitem.actions;

import java.util.Scanner;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;

public class AddNewStudentMenuItem extends MenuItem {
    private static final String MESSAGE_FIRST_NAME = "Input first name: ";
    private static final String MESSAGE_LAST_NAME = "Input last name: ";
    private static final String MASK_MESSAGE_ADD_STUDENT = "Student %s %s is created!";

    private StudentService studentService;
    private Scanner scanner;

    public AddNewStudentMenuItem(String name, StudentService studentService, Scanner scanner) {
	super(name);
	this.studentService = studentService;
	this.scanner = scanner;
    }

    @Override
    public void execute() {
	System.out.print(MESSAGE_FIRST_NAME);
	String firstName = scanner.nextLine();
	System.out.print(MESSAGE_LAST_NAME);
	String lastName = scanner.nextLine();
	studentService.createStudent(firstName, lastName);
	System.out.println(String.format(MASK_MESSAGE_ADD_STUDENT, firstName, lastName));
    }
}
