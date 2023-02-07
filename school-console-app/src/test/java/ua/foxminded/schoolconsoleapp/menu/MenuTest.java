package ua.foxminded.schoolconsoleapp.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.generatedata.service.CourseService;
import ua.foxminded.schoolconsoleapp.generatedata.service.GroupService;
import ua.foxminded.schoolconsoleapp.generatedata.service.StudentService;
import ua.foxminded.schoolconsoleapp.menu.menuitem.MenuItem;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.AddStudentToCourseMenuItem;
import ua.foxminded.schoolconsoleapp.menu.menuitem.actions.FindGroupsLessStudentsCountMenuItem;

@ExtendWith(MockitoExtension.class)
class MenuTest {
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private GroupService groupService;

    private Scanner scanner;
    private Menu menu;

    private ByteArrayInputStream testIn;

    @Test
    void shouldUseExecuteMethodWhenInitMenu() {
	String input = "1\n2\n2\n0";
	provideInput(input);
	scanner = new Scanner(testIn);

	MenuItem menuItemAddStudentToCourse = spy(
		new AddStudentToCourseMenuItem("Add Student to Course", courseService, studentService, scanner));
	MenuItem menuItemFindGroupsLessStudentsCountMenuItem = spy(new FindGroupsLessStudentsCountMenuItem("Find Groups", groupService, scanner));
	menu = spy(new Menu(scanner));

	menu.addMenuItem(1, menuItemAddStudentToCourse);
	menu.addMenuItem(2, menuItemFindGroupsLessStudentsCountMenuItem);
	menu.initMenu();

	verify(menu, times(1)).initMenu();
	verify(menuItemAddStudentToCourse, times(1)).execute();

    }

    @Test
    void shouldThrowNumberFormatExceptionWhenAddMenuItem() {
	String input = "g\n0";
	provideInput(input);
	scanner = new Scanner(testIn);

	MenuItem menuItemAddStudentToCourse = spy(new AddStudentToCourseMenuItem("Add Student to Course", courseService, studentService, scanner));
	MenuItem menuItemFindGroupsLessStudentsCountMenuItem = spy(new FindGroupsLessStudentsCountMenuItem("Find Groups", groupService, scanner));
	menu = spy(new Menu(scanner));

	menu.addMenuItem(1, menuItemAddStudentToCourse);
	menu.addMenuItem(2, menuItemFindGroupsLessStudentsCountMenuItem);
	
	doThrow(new NumberFormatException(MESSAGE_EXCEPTION_NOT_NUMBER)).when(menu).initMenu();
	Exception exception = assertThrows(NumberFormatException.class, () -> menu.initMenu());
	
	assertEquals(MESSAGE_EXCEPTION_NOT_NUMBER, exception.getMessage());
	verify(menu, times(1)).initMenu();
    }

    private void provideInput(String data) {
	testIn = new ByteArrayInputStream(data.getBytes());
	System.setIn(testIn);
    }
}
