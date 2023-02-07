package ua.foxminded.schoolconsoleapp.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class ReaderTest {
    private static final String FILE_PATH_COURSES = "src/test/resources/courses_test";
    private static final String BAD_FILE_PATH_COURSES = "src/test/resources/courses__test";
    private static final String FILE_PATH_PROPERTIES = "db.propertiesTest.properties";
    private static final String BAD_FILE_PATH_PROPERTIES = "propertiesTest";
    private static final String PROPERTY_DB_LOGIN = "dataSource.user";
    private static final String PROPERTY_DB_LOGIN_EXPECTED = "sa";

    Reader reader = new Reader();

    @Test
    void readShouldReturnExceptionIfInputFileIsNull() {
	Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
	    reader.read(null);
	});

	assertEquals("the file can't be null", exception.getMessage());
    }

    @Test
    void readShouldReturnExceptionIfBadFilePath() {
	Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
	    reader.read(BAD_FILE_PATH_COURSES);
	});

	assertEquals("Bad filePath. Please check it.", exception.getMessage());
    }

    @Test
    void readFileCoursesTest() {
	List<String> coursesTest = Arrays.asList("math, course of Mathematics");

	assertEquals(coursesTest, reader.read(FILE_PATH_COURSES));
    }

    @Test
    void readFilePropertiesTest() {
	Properties propertiesTest = reader.readProperties(FILE_PATH_PROPERTIES);
	String propertiesLoginExpected = PROPERTY_DB_LOGIN_EXPECTED;
	String propertiesLoginActual = propertiesTest.getProperty(PROPERTY_DB_LOGIN);

	assertEquals(propertiesLoginExpected, propertiesLoginActual);
    }

    @Test
    void readPropertiesShouldReturnExceptionIfInputFileIsNull() {
	Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
	    reader.readProperties(null);
	});

	assertEquals("Filename is null", exception.getMessage());
    }

    @Test
    void readPropertiesShouldReturnExceptionIfFileNotFound() {
	Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
	    reader.readProperties(BAD_FILE_PATH_PROPERTIES);
	});

	assertEquals("File " + "\"propertiesTest\"" + " not found", exception.getMessage());
    }
}
