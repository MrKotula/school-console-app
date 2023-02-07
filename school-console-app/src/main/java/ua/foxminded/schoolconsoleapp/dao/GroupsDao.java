package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public interface GroupsDao extends Daos<Group, Integer> {

    List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) throws DaosException;
}
