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
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.AddNewStudentMenuItem;

@ExtendWith(MockitoExtension.class)
class AddNewStudentMenuItemTest {
    private static final String NAME_MENU = "Name Menu";
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String LS = System.lineSeparator();

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private AddNewStudentMenuItem item;
    private Scanner scanner;

    @Mock
    StudentService service;

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
    void givenStringInput_whenCreateStudent_thenVerifyCallStudentServiceOneTime() {
        String input = TEST_FIRST_NAME + LS + TEST_LAST_NAME;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new AddNewStudentMenuItem(NAME_MENU, service, scanner);
        item.execute();
        
        verify(service, times(1)).createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
    }
}
