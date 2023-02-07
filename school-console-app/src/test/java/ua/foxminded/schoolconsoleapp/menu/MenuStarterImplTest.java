package ua.foxminded.schoolconsoleapp.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.viewprovider.ViewProvider;

@ExtendWith(MockitoExtension.class)
class MenuStarterImplTest {
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final int COURSE_ID_INT = 1;
    private static final int STUDENT_ID_INT = 5;
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";
    private static final String COURSE_NAME = "math";
    private static final int STUDENT_COUNT_INT = 25;

    @Mock
    GroupService groupService;

    @Mock
    StudentService studentService;

    @Mock
    CourseService courseService;

    @Mock
    ViewProvider viewProvider;

    @InjectMocks
    private MenuStarterImpl menuStarterImpl;

    private Scanner scanner;

    @Test
    void findAllStudentsToCourseNameCorrectResult() throws DaosException {
	List<Student> result = Arrays.asList(
		Student.builder()
		.withStudentId(1)
		.withGroupId(1).withFirstName("John")
		.withLastName("Dou")
		.build(),
		Student.builder()
		.withStudentId(2)
		.withGroupId(1)
		.withFirstName("Jane")
		.withLastName("Does")
		.build());
	String courseName = "math";

	when(viewProvider.readInt()).thenReturn(2).thenReturn(0);
	when(viewProvider.read()).thenReturn(courseName);
	when(studentService.getStudentsWithCourseName(courseName)).thenReturn(result);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).read();
	verify(studentService, times(1)).getStudentsWithCourseName("math");
    }

    @Test
    void ShouldReturnNewStudentWhenAddStudent() throws DaosException {
	String name = "Jane";
	String surname = "Does";

	when(viewProvider.readInt()).thenReturn(3).thenReturn(0);
	when(viewProvider.read()).thenReturn(name).thenReturn(surname);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentService, times(1)).createStudent(name, surname);
    }

    @Test
    void ShouldReturnStudentWhenRemoveStudent() throws DaosException {
	when(viewProvider.readInt()).thenReturn(4).thenReturn(2).thenReturn(0);
	menuStarterImpl.startMenu();

	verify(viewProvider, times(3)).readInt();
	verify(studentService, times(1)).deleteById(2);
    }

    @Test
    void ShouldReturnStudentsWhenAddtoCourse() throws DaosException {
	when(viewProvider.readInt()).thenReturn(5).thenReturn(2).thenReturn(1).thenReturn(0);
	menuStarterImpl.startMenu();

	verify(viewProvider, times(4)).readInt();
	verify(studentService, times(1)).addStudentToCourse(2, 1);
    }

    @Test
    void ShouldReturnStudentWhenRemoveFromCourse() throws DaosException {
	when(viewProvider.readInt()).thenReturn(6).thenReturn(2).thenReturn(1).thenReturn(0);
	menuStarterImpl.startMenu();

	verify(viewProvider, times(4)).readInt();
	verify(studentService, times(1)).removeStudentFromCourse(2, 1);
    }

    @Test
    void ShouldReturnDefaultMenuWhenInputWrongNumber() throws DaosException {
	String message = "Incorrect command\n";
	when(viewProvider.readInt()).thenReturn(7).thenReturn(0);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).printMessage(message);
    }

    @Test
    void givenStudentCountString_whenGetGroupsWithLessEqualsStudentCount_thenVerifyCallServicesOneTimeFromArgument()
	    throws DaosException {
	scanner = new Scanner(System.in);
	when(viewProvider.readInt()).thenReturn(1).thenReturn(0);

	menuStarterImpl.startMenu();
	ViewProvider view = new ViewProvider(scanner);
	view.printMessage("25");

	verify(groupService, times(1)).getGroupsWithLessEqualsStudentCount(0);
    }

    @Test
    void givenStringInput_whenAddStudentToCourse_thenVerifyCallServicesOneTimeFromArgument() throws DaosException {
	when(viewProvider.readInt()).thenReturn(STUDENT_COUNT_INT, COURSE_ID_INT);
	
        menuStarterImpl.addStudentToCourse();
        studentService.addStudentToCourse(STUDENT_ID_INT, COURSE_ID_INT);
        
        verify(studentService, times(1)).addStudentToCourse(STUDENT_ID_INT, COURSE_ID_INT);
    }

    @Test
    void givenStudentIdString_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() throws DaosException {
	when(viewProvider.readInt()).thenReturn(STUDENT_ID_INT);
	
        studentService.deleteById(STUDENT_ID_INT);
        menuStarterImpl.deleteStudentById();
        
        verify(studentService, times(2)).deleteById(STUDENT_ID_INT);
    }

    @Test
    void shouldThrowException_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() throws DaosException {
	doThrow(new InputMismatchException(MESSAGE_EXCEPTION_NOT_NUMBER)).when(studentService)
		.deleteById(STUDENT_ID_INT);
	Exception exception = assertThrows(InputMismatchException.class,
		() -> studentService.deleteById(STUDENT_ID_INT));

	assertEquals(MESSAGE_EXCEPTION_NOT_NUMBER, exception.getMessage());
	verify(studentService, times(1)).deleteById(STUDENT_ID_INT);
    }

    @Test
    void givenCourseName_whenGetStudentsWithCourseName_thenVerifyCallServicesOneTimeFromArgument() throws DaosException {
	String input = COURSE_NAME;
	when(viewProvider.read()).thenReturn(input);

	studentService.getStudentsWithCourseName(input);
	menuStarterImpl.findAllStudentsToCourseName();

	verify(studentService, times(2)).getStudentsWithCourseName(COURSE_NAME);
    }

    @Test
    void givenStringInput_whenRemoveStudentFromCourse_thenVerifyCallServicesOneTimeFromArgument() throws DaosException {
        when(viewProvider.readInt()).thenReturn(STUDENT_ID_INT).thenReturn(COURSE_ID_INT);
        
        studentService.removeStudentFromCourse(STUDENT_ID_INT, COURSE_ID_INT);
        menuStarterImpl.removeStudentFromCourse();
        
        verify(studentService, times(2)).removeStudentFromCourse(STUDENT_ID_INT, COURSE_ID_INT);
    }

    @Test
    void givenStringInput_whenCreateStudent_thenVerifyCallStudentServiceOneTime() throws DaosException {
        when(viewProvider.read()).thenReturn(TEST_FIRST_NAME).thenReturn(TEST_LAST_NAME);
        
        menuStarterImpl.addStudent();
        studentService.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
        
        verify(studentService, times(2)).createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
    }
}
