package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.entity.Student;

@ExtendWith(MockitoExtension.class)
class StudentGeneratorTest {
    private static final String FIRST_NAME = "Noah";
    private static final String LAST_NAME = "Doe";
    private static final String TEST_FIRST_NAME = "Oliver";
    private static final String TEST_LAST_NAME = "Smith";
    
    @Mock
    private Random random;
    
    @InjectMocks
    private Generator<Student> studentGenerator = new StudentGenerator(random);
    
    private Student testStudent = Student.builder()
	    .withGroupId(1)
	    .withFirstName(FIRST_NAME)
	    .withLastName(LAST_NAME)
	    .build();
    
    private Student testSecondStudent = Student.builder()
	    .withFirstName(TEST_FIRST_NAME)
	    .withLastName(TEST_LAST_NAME)
	    .build();
    
    @Test
    void shouldReturnGeneratedStudents() {
	when(random.nextInt(anyInt())).thenReturn(1);
	List<Student> expectedStudents = new ArrayList<>();
	
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	expectedStudents.add(testStudent);
	
	List<Student> actualStudent = studentGenerator.generate(11);
	
	assertEquals(expectedStudents, actualStudent);
    }   
    
    @Test
    void shouldResturnGeneratedStudent() {
	when(random.nextInt(anyInt())).thenReturn(2);
	List<Student> expectedStudents = new ArrayList<>();
	
	expectedStudents.add(testSecondStudent);
	List<Student> actualStudent = studentGenerator.generate(1);
	
	assertEquals(expectedStudents, actualStudent);
    } 
}
