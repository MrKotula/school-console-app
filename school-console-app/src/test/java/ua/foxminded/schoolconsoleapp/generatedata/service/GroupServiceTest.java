package ua.foxminded.schoolconsoleapp.generatedata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.foxminded.schoolconsoleapp.dao.Daos;
import ua.foxminded.schoolconsoleapp.dao.GroupsDao;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    private static final String TEST_GROUP_NAME = "PL-29";
    private static final int NUMBERS_OF_GROUPS = 5;
    private static final int STUDENT_COUNT_EXCEPTION = 27;
    private static final String MESSAGE_GET_EXCEPTION = "Can't get groups";
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save group %s in base";

    @Mock
    private GroupsDao groupsDao;

    @Mock
    private Generator<Group> groupGenerator;

    @InjectMocks
    private GroupService groupService;

    private List<Group> groups = new ArrayList<>();
    private Group group = Group.builder()
	    .withGroupName(TEST_GROUP_NAME)
	    .build();

    @Test
    void shouldReturnTestGroups() {
	groupService.createTestGroups(NUMBERS_OF_GROUPS);
	verify(groupGenerator, times(1)).generate(NUMBERS_OF_GROUPS);
    }

    @Test
    void shouldReturnGetGroupWithLessStudents() throws DaosException {
	when(groupsDao.getGroupsWithLessEqualsStudentCount(STUDENT_COUNT_EXCEPTION)).thenThrow(DaosException.class);
	Exception exception = assertThrows(DaosException.class, () -> groupService.getGroupsWithLessEqualsStudentCount(STUDENT_COUNT_EXCEPTION));
	
	assertEquals(MESSAGE_GET_EXCEPTION, exception.getMessage());
	verify(groupsDao, times(1)).getGroupsWithLessEqualsStudentCount(STUDENT_COUNT_EXCEPTION);
    }

    @Test
    void shouldReturnGetGroupWithLessStudentsThenReturnListOfStudents() throws DaosException {
	when(groupsDao.getGroupsWithLessEqualsStudentCount(anyInt())).thenReturn(groups);
	List<Group> actualGroups = groupService.getGroupsWithLessEqualsStudentCount(anyInt());
	
	verify(groupsDao, times(1)).getGroupsWithLessEqualsStudentCount(anyInt());
        assertEquals(groups, actualGroups);
    }

    @Test
    void shouldReturnDaoExceptionIfAddGroupToBase() throws DaosException {
	doThrow(DaosException.class).when(groupsDao).add(group);
	Exception exception = assertThrows(DaosException.class, () -> groupService.addGroupToBase(group));
	String expectedMessage = String.format(MASK_MESSAGE_ADD_EXCEPTION, group);
	verify(groupsDao, times(1)).add(any());

	assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void givenAddedCourses_whenGenerateNumberCourses_thenCourseDaoIsCalledAsMuchTimeAsNumberCourses() {
	for (int i = 0; i < NUMBERS_OF_GROUPS; i++) {
	    groups.add(group);
	}
	when(groupGenerator.generate(NUMBERS_OF_GROUPS)).thenReturn(groups);
	groupService.createTestGroups(NUMBERS_OF_GROUPS);
	
	verify(groupGenerator, times(1)).generate(NUMBERS_OF_GROUPS);
    }
}
