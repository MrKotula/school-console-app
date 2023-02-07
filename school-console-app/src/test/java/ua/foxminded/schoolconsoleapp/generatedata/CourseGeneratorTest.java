package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.entity.Course;

class CourseGeneratorTest {
    private Generator<Course> courseGenerator = new CourseGenerator();
    
    @Test
    void shouldReturnGeneratedCourses() {
	List<Course> listOfCoursesExpected = new ArrayList<>();
	
	listOfCoursesExpected.add(new Course("math", "course of Mathematics"));
	listOfCoursesExpected.add(new Course("biology", "course of Biology"));
	listOfCoursesExpected.add(new Course("chemistry", "course of Chemistry"));
	List<Course> listOfCourses = courseGenerator.generate(3);
	
	assertEquals(listOfCoursesExpected, listOfCourses);
    }
}
