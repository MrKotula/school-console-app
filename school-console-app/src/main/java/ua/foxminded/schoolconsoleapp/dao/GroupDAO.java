package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

public interface GroupDAO extends DAO<Group, Integer> {
    
    List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) throws DAOException;
}
