package ua.foxminded.schoolconsoleapp.generatedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.entity.Group;

@ExtendWith(MockitoExtension.class)
class CroupGeneratorTest {
    private static final String TEST_GROUP_NAME = "-00";

    @Spy
    private Generator<Group> groupGenerator = new GroupGenerator();

    private Group testGroup = Group.builder()
	    .withGroupId(1)
	    .withGroupName(TEST_GROUP_NAME)
	    .build();
    private List<Group> testList = Arrays.asList(testGroup);

    @Test
    void shouldReturnGeneratedTestGroup() {
	when(groupGenerator.generate(1)).thenReturn(testList);
	List<Group> expectedGroup = new ArrayList<>();
	expectedGroup.add(testGroup);
	
	assertEquals(expectedGroup, groupGenerator.generate(1));
	verify(groupGenerator, times(1)).generate(1);
    }
}
