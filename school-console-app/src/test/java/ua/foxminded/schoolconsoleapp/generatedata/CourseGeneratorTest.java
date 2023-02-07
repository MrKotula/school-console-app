package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.reader.Reader;

class CourseGeneratorTest {
    private Generator<Course> courseGenerator = new CourseGenerator(new Reader());
    private Course testCourseMath = Course.builder()
	    .withCourseName("math")
	    .withCourseDescription("course of Mathematics")
	    .build();
    
    private Course testCourseBiology = Course.builder()
	    .withCourseName("biology")
	    .withCourseDescription("course of Biology")
	    .build();
    
    private Course testCourseChemistry = Course.builder()
	    .withCourseName("chemistry")
	    .withCourseDescription("course of Chemistry")
	    .build();
    
    @Test
    void shouldReturnGeneratedCourses() {
	List<Course> listOfCoursesExpected = new ArrayList<>();
	
	listOfCoursesExpected.add(testCourseMath);
	listOfCoursesExpected.add(testCourseBiology);
	listOfCoursesExpected.add(testCourseChemistry);
	List<Course> listOfCourses = courseGenerator.generate(3);
	
	assertEquals(listOfCoursesExpected, listOfCourses);
    }
}
