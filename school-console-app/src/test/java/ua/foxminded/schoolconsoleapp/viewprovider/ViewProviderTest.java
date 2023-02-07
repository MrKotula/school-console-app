package ua.foxminded.schoolconsoleapp.viewprovider;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ViewProviderTest {

    @Mock
    private Scanner scanner;

    @InjectMocks
    private ViewProvider viewProvider;

    @Test
    void printMessageShouldReturnMessageFromConsole() {
	ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	System.setOut(new PrintStream(outputStreamCaptor));

	viewProvider.printMessage("word");

	assertThat("word").isEqualTo(outputStreamCaptor.toString().trim());
    }

    @Test
    void readIntShouldReturnIntegerFromScannerMocked() {
	int expected = 5;
	when(scanner.nextInt()).thenReturn(5);

	viewProvider.printMessage("5");
	int actual = viewProvider.readInt();

	assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readStringShouldReturnNextLineFromScannerMocked() {
	String input = "hello";
	when(scanner.next()).thenReturn("hello");

	viewProvider.printMessage(input);
	input = viewProvider.read();

	assertThat("hello").isEqualTo(input);
    }

    @Test
    void readStringShouldReturnHasNextFromScannerMocked() {
	when(scanner.hasNext()).thenReturn(true);
	boolean input = true;
	input = viewProvider.readBoolean();

	assertTrue(input);
    }
}
