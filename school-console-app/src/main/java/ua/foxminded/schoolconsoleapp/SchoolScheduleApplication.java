package ua.foxminded.schoolconsoleapp;

import java.util.Scanner;
import ua.foxminded.schoolconsoleapp.applicationfacade.ApplicationFacade;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.CoursesDao;
import ua.foxminded.schoolconsoleapp.dao.GroupsDao;
import ua.foxminded.schoolconsoleapp.dao.StartsDao;
import ua.foxminded.schoolconsoleapp.dao.StudentsDao;
import ua.foxminded.schoolconsoleapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolconsoleapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolconsoleapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.CourseGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;
import ua.foxminded.schoolconsoleapp.generatedata.GroupGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.StudentGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.MenuStarter;
import ua.foxminded.schoolconsoleapp.menu.MenuStarterImpl;
import ua.foxminded.schoolconsoleapp.reader.Reader;
import ua.foxminded.schoolconsoleapp.viewprovider.ViewProvider;

public class SchoolScheduleApplication {
    private static final String FILEPATH_DB_PROPERTIES = "src/main/resources/db.properties";

    public static void main(String[] args) throws DaosException {
	ConnectionProvider connectionProvider = new ConnectionProvider(FILEPATH_DB_PROPERTIES);
	StartsDao startsDao = new StartsDao(connectionProvider);

	GroupsDao groupsDao = new GroupDaoImpl(connectionProvider);
	CoursesDao coursesDao = new CourseDaoImpl(connectionProvider);
	StudentsDao studentsDao = new StudentDaoImpl(connectionProvider);

	Generator<Group> groupGenerator = new GroupGenerator();
	Generator<Course> coursGenerator = new CourseGenerator(new Reader());
	Generator<Student> studentGenerator = new StudentGenerator(new Reader());

	GroupService groupService = new GroupService(groupsDao, groupGenerator);
	CourseService courseService = new CourseService(coursesDao, coursGenerator);
	StudentService studentService = new StudentService(studentsDao, studentGenerator);

	Scanner scanner = new Scanner(System.in);
	ViewProvider viewProvider = new ViewProvider(scanner);
	MenuStarter menuStarterImpl = new MenuStarterImpl(groupService, studentService, courseService, viewProvider);
	ApplicationFacade facade = new ApplicationFacade(startsDao, groupService, courseService, studentService, menuStarterImpl);

	facade.runApp();
    }
}
