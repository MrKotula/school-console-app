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
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.RemoveStudentFromCourseMenuItem;

@ExtendWith(MockitoExtension.class)
class RemoveStudentFromCourseMenuItemTest {
    private static final String STUDENT_ID_STRING = "5";
    private static final int STUDENT_ID_INT = 5;
    private static final String COURSE_ID_STRING = "2";
    private static final int COURSE_ID_INT = 2;
    private static final String NAME_MENU = "Name Menu";
    private static final String LS = System.lineSeparator();

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private RemoveStudentFromCourseMenuItem item;
    private Scanner scanner;

    @Mock
    StudentService studentService;

    @Mock
    CourseService courseService;

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
    void givenStringInput_whenRemoveStudentFromCourse_thenVerifyCallServicesOneTimeFromArgument() {
        String input = STUDENT_ID_STRING + LS + COURSE_ID_STRING;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new RemoveStudentFromCourseMenuItem(NAME_MENU, courseService, studentService, scanner);
        item.execute();
        
        verify(courseService, times(1)).getCoursesForStudent(STUDENT_ID_INT);
        verify(studentService, times(1)).removeStudentFromCourse(STUDENT_ID_INT, COURSE_ID_INT);
    }
}
