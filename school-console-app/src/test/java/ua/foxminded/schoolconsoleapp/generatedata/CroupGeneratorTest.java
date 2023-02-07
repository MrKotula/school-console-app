package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.entity.Group;

@ExtendWith(MockitoExtension.class)
class CroupGeneratorTest {
    private static final String TEST_GROUP_NAME = "-00";
    
    @Mock
    private Random random;
    
    @InjectMocks
    private Generator<Group> groupGenerator = new GroupGenerator(random);
    
    @Test
    void shouldReturnGeneratedTestGroup() {
	List<Group> expectedGroup = new ArrayList<>();
	expectedGroup.add(new Group(1, TEST_GROUP_NAME));
	List<Group> actualGroup = groupGenerator.generate(1);
	
	assertEquals(expectedGroup, actualGroup);
    }
}
