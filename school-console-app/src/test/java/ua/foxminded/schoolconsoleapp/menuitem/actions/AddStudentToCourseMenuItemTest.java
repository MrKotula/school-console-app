package ua.foxminded.schoolconsoleapp.menuitem.actions;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.AddStudentToCourseMenuItem;

@ExtendWith(MockitoExtension.class)
class AddStudentToCourseMenuItemTest {
    private static final int COURSE_ID_INT = 1;
    private static final int STUDENT_ID_INT = 5;
    private static final String COURSE_ID_STRING = "1";
    private static final String STUDENT_ID_STRING = "5";
    private static final String NAME_MENU = "Name Menu";
    private static final String LS = System.lineSeparator();

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private AddStudentToCourseMenuItem item;
    private Scanner scanner;

    @Mock
    CourseService courseService;

    @Mock
    StudentService studentService;

    @BeforeEach
    void setUpOutput() throws Exception {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreSystemInputOutput() throws Exception {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void givenStringInput_whenAddStudentToCourse_thenVerifyCallServicesOneTimeFromArgument() {
        String input = STUDENT_ID_STRING + LS + COURSE_ID_STRING;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new AddStudentToCourseMenuItem(NAME_MENU, courseService,
                studentService, scanner);
        item.execute();
        
        verify(courseService, times(1)).getCoursesMissingForStudent(STUDENT_ID_INT);
        verify(studentService, times(1)).addStudentToCourse(STUDENT_ID_INT, COURSE_ID_INT);
    }
}
