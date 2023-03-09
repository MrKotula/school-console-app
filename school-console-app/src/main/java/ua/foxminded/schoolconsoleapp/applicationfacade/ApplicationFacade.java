package ua.foxminded.schoolconsoleapp.applicationfacade;

import ua.foxminded.schoolconsoleapp.dao.StartDAO;
import ua.foxminded.schoolconsoleapp.exception.DAOException;
import ua.foxminded.schoolconsoleapp.exception.DomainException;
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
    private static final String MESSAGE_EXCEPTION_CREATE_TABLES = "Can't delete and create tables";

    private StartDAO startDAO;
    private GroupService groupService;
    private CourseService courseService;
    private StudentService studentService;

    public ApplicationFacade(StartDAO startDAO, GroupService groupService,
            CourseService courseService, StudentService studentService) {
        this.startDAO = startDAO;
        this.groupService = groupService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public void prepareBase() throws DomainException {
        System.out.println(MESSAGE_START_PREPARE);
        fillTable();
        System.out.println(MESSAGE_FINISH_PREPARE);
    }

    public void workWithBase() {
        MenuStarter menu = new MenuStarter(groupService, studentService, courseService);
        menu.startMenu();
    }

    private void fillTable() {
        groupService.createTestGroups(NUMBER_GROUPS);
        courseService.createTestCourses(NUMBER_COURSES);
        studentService.createTestStudents(NUMBER_STUDENTS);
        studentService.createTestStudentsCourses(NUMBER_STUDENTS);
    }
}
