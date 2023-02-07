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
    
    @Mock
    private Random random;
    
    @InjectMocks
    private Generator<Student> studentGenerator = new StudentGenerator(random);
    
    @Test
    void shouldReturnGeneratedStudents() {
	when(random.nextInt(anyInt())).thenReturn(1);
	List<Student> expectedStudents = new ArrayList<>();
	
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	expectedStudents.add(new Student(1, FIRST_NAME, LAST_NAME));
	
	List<Student> actualStudent = studentGenerator.generate(11);
	assertEquals(expectedStudents, actualStudent);
    }   
    
    @Test
    void shouldResturnGeneratedStudent() {
	when(random.nextInt(anyInt())).thenReturn(2);
	List<Student> expectedStudents = new ArrayList<>();
	
	expectedStudents.add(new Student("Oliver", "Smith"));
	
	List<Student> actualStudent = studentGenerator.generate(1);
	assertEquals(expectedStudents, actualStudent);
    } 
}
