package ua.foxminded.schoolconsoleapp.applicationfacade;

import ua.foxminded.schoolconsoleapp.dao.StartsDao;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.MenuStarter;

public class ApplicationFacade {
    private static final String MESSAGE_START_PREPARE = "Starting to prepare the base. It can take some time ...";
    private static final String MESSAGE_FINISH_PREPARE = "Database prepared";
    private static final int NUMBER_STUDENTS = 200;
    private static final int NUMBER_COURSES = 10;
    private static final int NUMBER_GROUPS = 10;

    private final StartsDao startsDao;
    private final GroupService groupService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final MenuStarter menuStarter;

    public ApplicationFacade(StartsDao startsDao, GroupService groupService, CourseService courseService,
	    StudentService studentService, MenuStarter menuStarter) {
	this.startsDao = startsDao;
	this.groupService = groupService;
	this.courseService = courseService;
	this.studentService = studentService;
	this.menuStarter = menuStarter;
    }

    public void runApp() throws DaosException {
	System.out.println(MESSAGE_START_PREPARE);
	fillTable();
	System.out.println(MESSAGE_FINISH_PREPARE + "\n");
	menuStarter.startMenu();
    }

    private void fillTable() throws DaosException {
	groupService.createTestGroups(NUMBER_GROUPS);
	courseService.createTestCourses(NUMBER_COURSES);
	studentService.createTestStudents(NUMBER_STUDENTS);
	studentService.createTestStudentsCourses(NUMBER_STUDENTS);
    }
}
