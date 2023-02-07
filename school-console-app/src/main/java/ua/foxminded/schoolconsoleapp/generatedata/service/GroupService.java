package ua.foxminded.schoolconsoleapp.generatedata.service;

import java.util.List;
import ua.foxminded.schoolconsoleapp.dao.GroupDAO;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DAOException;
import ua.foxminded.schoolconsoleapp.exception.DomainException;
import ua.foxminded.schoolconsoleapp.generatedata.Generator;

public class GroupService {
    private static final String MASK_MESSAGE_ADD_EXCEPTION = "Don't save group %s in base";
    private static final String MESSAGE_GET_EXCEPTION = "Can't get groups";

    private GroupDAO groupDao;
    private Generator<Group> generator;

    public GroupService(GroupDAO groupDao, Generator<Group> generator) {
	this.groupDao = groupDao;
	this.generator = generator;
    }

    public void createTestGroups(int numberGroups) {
	List<Group> groups = generator.generate(numberGroups);
	groups.forEach(this::addGroupToBase);
    }

    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) {

	try {
	    return groupDao.getGroupsWithLessEqualsStudentCount(studentCount);
	} catch (DAOException e) {
	    throw new DomainException(MESSAGE_GET_EXCEPTION, e);
	}
    }

    public void addGroupToBase(Group group) {
	try {
	    groupDao.add(group);
	} catch (DomainException e) {
	    throw new DomainException(String.format(MASK_MESSAGE_ADD_EXCEPTION, group), e);
	}
    }
}
