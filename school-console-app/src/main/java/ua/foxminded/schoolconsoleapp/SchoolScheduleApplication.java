package ua.foxminded.schoolconsoleapp;

import java.util.Random;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.CourseDAO;
import ua.foxminded.schoolconsoleapp.dao.GroupDAO;
import ua.foxminded.schoolconsoleapp.dao.StartDAO;
import ua.foxminded.schoolconsoleapp.dao.StudentDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCCourseDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCGroupDAO;
import ua.foxminded.schoolconsoleapp.dao.jdbc.JDBCStudentDAO;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.facade.Facade;
import ua.foxminded.schoolconsoleapp.generatedata.CourseGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;
import ua.foxminded.schoolconsoleapp.generatedata.GroupGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.StudentGenerator;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;

public class SchoolScheduleApplication {
    public static void main(String[] args) {
	StartDAO startDAO = new StartDAO();

	GroupDAO groupDAO = new JDBCGroupDAO(new ConnectionProvider());
	CourseDAO courseDAO = new JDBCCourseDAO(new ConnectionProvider());
	StudentDAO studentDao = new JDBCStudentDAO(new ConnectionProvider());
	
	Generator<Group> groupGenerator = new GroupGenerator(new Random());
	Generator<Course> coursGenerator = new CourseGenerator();
	Generator<Student> studentGenerator = new StudentGenerator(new Random());
	
	GroupService groupService = new GroupService(groupDAO, groupGenerator);
	CourseService courseService = new CourseService(courseDAO, coursGenerator);
	StudentService studentService = new StudentService(studentDao, studentGenerator, new Random());
	
	Facade facade = new Facade(startDAO, groupService, courseService, studentService);
	
	facade.prepareBase();
	facade.workWithBase();
    }
}
