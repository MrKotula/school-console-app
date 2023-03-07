package ua.foxminded.schoolconsoleapp.generatedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.reader.Reader;

public class StudentGenerator implements Generator<Student> {
    private static final String FILENAME_FIRST_NAMES_DATA = "src/main/resources/student_first_names.txt";
    private static final String FILENAME_LAST_NAMES_DATA = "src/main/resources/student_last_names.txt";
    private static final int STUDENT_FIRST_NAME = 0;
    private static final int STUDENT_LAST_NAME = 1;
    
    private static final int AMOUNT_OF_STUDENTS = 20;
    private static final int MIN_GROUP_SIZE = 19;
    private static final int MAX_GROUP_SIZE = 30;
    
    private Random random = new Random();
    private Reader reader = new Reader();
    
    public StudentGenerator(Random random) {
	this.random = random;
    }
    
    public List<Student> generate(int amountOfStudents) {
	List<Student> students = new ArrayList<>();

	for (int i = 0; i < amountOfStudents; i++) {
	    String[] studentsNames = createStudentNames();
	    Student student = Student.builder()
		    .withFirstName(studentsNames[STUDENT_FIRST_NAME])
		    .withLastName(studentsNames[STUDENT_LAST_NAME])
		    .build();
		    
	    students.add(student);
	}
	return splitStudentsToGroups(students);
    }
    

    private String[] createStudentNames() {
	List<String> studentFirstNames = reader.read(FILENAME_FIRST_NAMES_DATA);
	List<String> studentLastNames = reader.read(FILENAME_LAST_NAMES_DATA);

	String[] students = new String[2];
	students[0] = studentFirstNames.get(random.nextInt(AMOUNT_OF_STUDENTS));
	students[1] = studentLastNames.get(random.nextInt(AMOUNT_OF_STUDENTS));

	return students;
    }

    private List<Student> splitStudentsToGroups(List<Student> students) {
	int numberStudents = students.size();
	int[] sizeGroups = calculateSizeGroups(numberStudents);

	for (Student student : students) {
	    for (int i = 0; i < sizeGroups.length; i++) {
		if (sizeGroups[i] != 0) {
		    student.setGroupId(i + 1);
		    sizeGroups[i]--;
		    break;
		}
	    }
	}
	return students;
    }

    private int[] calculateSizeGroups(int numberStudents) {
	int[] numberStudentInGroups = new int[10];
	List<Integer> variantSizes = Stream.iterate(0, n -> n + 1).limit(MAX_GROUP_SIZE + 1l)
		.filter(n -> n == 0 || n > MIN_GROUP_SIZE).collect(Collectors.toList());
	for (int i = 0; i < numberStudentInGroups.length; i++) {
	    if (numberStudents > 10) {
		numberStudentInGroups[i] = variantSizes.get(random.nextInt(variantSizes.size()));
	    } else {
		numberStudentInGroups[i] = 0;
	    }
	    numberStudents -= numberStudentInGroups[i];
	}
	return numberStudentInGroups;
    }
}
