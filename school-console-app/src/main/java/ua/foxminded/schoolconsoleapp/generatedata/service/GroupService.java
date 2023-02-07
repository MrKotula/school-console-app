package ua.foxminded.schoolconsoleapp.generatedata.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.schoolconsoleapp.dao.GroupsDao;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DaosException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

public class GroupService {
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save group %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get groups";
    
    private static Logger logger = LoggerFactory.getLogger(GroupService.class);
    private GroupsDao groupsDao;
    private Generator<Group> generator;

    public GroupService(GroupsDao groupsDao, Generator<Group> generator) {
	this.groupsDao = groupsDao;
	this.generator = generator;
    }

    public void createTestGroups(int numberGroups) {
	List<Group> groups = generator.generate(numberGroups);
	groups.forEach(t -> {
	    try {
		addGroupToBase(t);
	    } catch (DaosException e) {
		logger.info(e.getMessage());
	    }
	});
    }

    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) throws DaosException {
	try {
	    return groupsDao.getGroupsWithLessEqualsStudentCount(studentCount);
	} catch (DaosException e) {
	    throw new DaosException(MESSAGE_GET_EXCEPTION, e);
	}
    }

    public void addGroupToBase(Group group) throws DaosException {
	try {
	    groupsDao.add(group);
	} catch (DaosException e) {
	    throw new DaosException(String.format(MASK_MESSAGE_ADD_EXCEPTION, group), e);
	}
    }
}
