package ua.foxminded.schoolconsoleapp.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class Reader {
    private static final String EXCEPTION_WRONG_FILE_PATH = "Bad filePath. Please check it.";
    private static final String MESSAGE_FILENAME_IS_NULL = "Filename is null";
    private static final String MASK_MESSAGE_FILE_NOT_FOUND = "File \"%s\" not found";

    public List<String> read(String fileName) {
	try {
	    if (fileName == null) {
		throw new IllegalArgumentException("the file can't be null");
	    }

	    return Files.lines(Paths.get(fileName)).collect(Collectors.toList());
	} catch (IOException e) {
	    throw new IllegalArgumentException(EXCEPTION_WRONG_FILE_PATH, e);
	}
    }
    
    public Properties readProperties(String filename)  {
        if (Objects.isNull(filename)) {
            throw new IllegalArgumentException(MESSAGE_FILENAME_IS_NULL);
        }
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(filename)) {

            properties.load(input);
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException(
                    String.format(MASK_MESSAGE_FILE_NOT_FOUND, filename), e);
        }
        
        return properties;
    }
}
