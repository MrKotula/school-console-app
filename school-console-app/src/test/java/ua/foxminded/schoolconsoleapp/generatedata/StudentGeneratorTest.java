package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.reader.Reader;

@ExtendWith(MockitoExtension.class)
class StudentGeneratorTest {
    private static final String TEST_FIRST_NAME = "Oliver";
    private static final String TEST_LAST_NAME = "Smith";

    @Spy
    private Generator<Student> studentGenerator = new StudentGenerator(new Reader());
    private Student testSecondStudent = Student.builder()
	    .withFirstName(TEST_FIRST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();
    private List<Student> testListSecond = Arrays.asList(testSecondStudent);

    @Test
    void shouldReturnGeneratedStudents() {
	studentGenerator.generate(11);

	verify(studentGenerator, times(1)).generate(11);
    }

    @Test
    void shouldResturnGeneratedStudent() {
	when(studentGenerator.generate(1)).thenReturn(testListSecond);
	List<Student> expectedStudents = new ArrayList<>();
	expectedStudents.add(testSecondStudent);
	List<Student> actualStudent = studentGenerator.generate(1);
	
	assertEquals(testListSecond, actualStudent);
	verify(studentGenerator, times(1)).generate(1);
    }
}
