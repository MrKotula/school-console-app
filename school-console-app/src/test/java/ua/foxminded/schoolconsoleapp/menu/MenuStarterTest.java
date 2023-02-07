package ua.foxminded.schoolconsoleapp.menu;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.AddStudentToCourseMenuItem;

@ExtendWith(MockitoExtension.class)
class MenuStarterTest {
    
    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    MenuStarter menuStarter = new MenuStarter(groupService, studentService, courseService);

    private Scanner scanner;
    private Menu menu;
    private ByteArrayInputStream testIn;

    @Test
    void shouldReturnMenuTest() {
	scanner = new Scanner(System.in);
	MenuItem menuItemAddStudentToCourse = new AddStudentToCourseMenuItem("Menu", courseService, studentService, scanner);

	provideInput("0");
	menu = mock(Menu.class);

	menuStarter.startMenu();
	verify(menu, times(0)).addMenuItem(5, menuItemAddStudentToCourse);
    }

    private void provideInput(String data) {
	testIn = new ByteArrayInputStream(data.getBytes());
	System.setIn(testIn);
    }
}
