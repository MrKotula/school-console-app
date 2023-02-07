package ua.foxminded.schoolconsoleapp.generatedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.reader.Reader;

public class StudentGenerator implements Generator<Student> {
    private static final String FILENAME_FIRST_NAMES_DATA = "src/main/resources/student_first_names.txt";
    private static final String FILENAME_LAST_NAMES_DATA = "src/main/resources/student_last_names.txt";
    private static final int STUDENT_FIRST_NAME = 0;
    private static final int STUDENT_LAST_NAME = 1;
    private static final int AMOUNT_OF_STUDENTS = 20;
    
    private Reader reader;
    
    public StudentGenerator(Reader reader) {
	this.reader = reader;
    }
    
    public List<Student> generate(int amountOfStudents) {
	List<Student> students = new ArrayList<>();

	for (int i = 0; i < amountOfStudents; i++) {
	    String[] studentsNames = createStudentNames();
	    Student student = Student.builder()
		    .withStudentId(i)
		    .withFirstName(studentsNames[STUDENT_FIRST_NAME])
		    .withLastName(studentsNames[STUDENT_LAST_NAME])
		    .withGroupId(randomGroupNumber())
		    .build();
		    
	    students.add(student);
	}
	return students;
    }
   
    private String[] createStudentNames() {
	Random random = new Random();
	List<String> studentFirstNames = reader.read(FILENAME_FIRST_NAMES_DATA);
	List<String> studentLastNames = reader.read(FILENAME_LAST_NAMES_DATA);

	String[] students = new String[2];
	students[0] = studentFirstNames.get(random.nextInt(AMOUNT_OF_STUDENTS));
	students[1] = studentLastNames.get(random.nextInt(AMOUNT_OF_STUDENTS));

	return students;
    }
    
    private Integer randomGroupNumber() {
        return (int) (Math.random() * 10) + 1;
    }
}
