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
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.FindGroupsLessStudentsCountMenuItem;

@ExtendWith(MockitoExtension.class)
class FindGroupsLessStudentsCountMenuItemTest {
    private static final String NAME_MENU = "Name Menu";
    private static final String STUDENT_COUNT_STRING = "25";
    private static final int STUDENT_COUNT_INT = 25;

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private FindGroupsLessStudentsCountMenuItem item;
    private Scanner scanner;

    @Mock
    GroupService groupService;

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
    void givenStudentCountString_whenGetGroupsWithLessEqualsStudentCount_thenVerifyCallServicesOneTimeFromArgument() {
        String input = STUDENT_COUNT_STRING;
        provideInput(input);
        scanner = new Scanner(testIn);
        item = new FindGroupsLessStudentsCountMenuItem(NAME_MENU, groupService, scanner);
        item.execute();
        
        verify(groupService, times(1)).getGroupsWithLessEqualsStudentCount(STUDENT_COUNT_INT);
    }
}
