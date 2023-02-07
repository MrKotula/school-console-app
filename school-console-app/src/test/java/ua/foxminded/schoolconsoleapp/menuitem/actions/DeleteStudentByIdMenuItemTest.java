package ua.foxminded.schoolconsoleapp.menuitem.actions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.DeleteStudentByIdMenuItem;

@ExtendWith(MockitoExtension.class)
class DeleteStudentByIdMenuItemTest {
    private static final String STUDENT_ID_STRING = "5";
    private static final int STUDENT_ID_INT = 5;
    private static final String NAME_MENU = "Name Menu";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private DeleteStudentByIdMenuItem item;
    private Scanner scanner;

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
    void givenStudentIdString_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
        String input = STUDENT_ID_STRING;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new DeleteStudentByIdMenuItem(NAME_MENU, studentService, scanner);
        item.execute();
        
        verify(studentService, times(1)).deleteById(STUDENT_ID_INT);
    }
    
    @Test
    void shouldThrowException_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
	provideInput(STUDENT_ID_STRING);
        scanner = new Scanner(testIn);
        doThrow(new NumberFormatException(MESSAGE_EXCEPTION_NOT_NUMBER)).when(studentService).deleteById(STUDENT_ID_INT);
        item = new DeleteStudentByIdMenuItem(NAME_MENU, studentService, scanner);
        
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> item.execute());

        assertEquals(MESSAGE_EXCEPTION_NOT_NUMBER, exception.getMessage());
        verify(studentService, times(1)).deleteById(STUDENT_ID_INT);
    }
}
