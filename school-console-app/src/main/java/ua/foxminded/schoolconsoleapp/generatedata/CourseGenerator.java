package ua.foxminded.schoolconsoleapp.generatedata;

import java.util.List;
import java.util.stream.Collectors;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.reader.Reader;

public class CourseGenerator implements Generator<Course> {
    private static final String FILENAME_COURSES_DATA = "src/main/resources/courses.txt";
    private static final String DELIMITER = ",";
    private static final int COURSE_NAME = 0;
    private static final int COURSE_DESCRIPTION = 1;

    private Reader reader;

    public CourseGenerator(Reader reader) {
	this.reader = reader;
    }

    public List<Course> generate(int number) {
	List<String> lines = reader.read(FILENAME_COURSES_DATA);

	return lines
		.stream().map(line -> line.split(DELIMITER)).limit(number).map(arr -> Course.builder()
			.withCourseName(arr[COURSE_NAME])
			.withCourseDescription(arr[COURSE_DESCRIPTION])
			.build())
		.collect(Collectors.toList());
    }
}
