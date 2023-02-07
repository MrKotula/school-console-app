package ua.foxminded.schoolconsoleapp.menu.menuitem.actions;

import java.util.List;
import java.util.Scanner;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;

public class FindGroupsLessStudentsCountMenuItem extends MenuItem {
    private static final String MESSAGE_INPUT_STUDENT_COUNT = "Input student count: ";
    
    private GroupService groupService;
    private Scanner scanner;
    
    public FindGroupsLessStudentsCountMenuItem(String name, GroupService groupService, Scanner scanner) {
	super(name);
	this.groupService = groupService;
	this.scanner = scanner;
    }

    @Override
    public void execute() {
	int studentCount = inputStudentCount();
        List<Group> groups = groupService.getGroupsWithLessEqualsStudentCount(studentCount);
        groups.stream().forEach(System.out::println);
    }

    private int inputStudentCount() {
        int studentCount = 0;
        System.out.print(MESSAGE_INPUT_STUDENT_COUNT);
        while (scanner.hasNext()) {
            studentCount = Integer.parseInt(scanner.nextLine());
            break;
        }
        
        return studentCount;
    }
}
