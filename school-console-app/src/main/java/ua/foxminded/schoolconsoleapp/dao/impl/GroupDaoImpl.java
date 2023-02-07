package ua.foxminded.schoolconsoleapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolconsoleapp.dao.ConnectionProvider;
import ua.foxminded.schoolconsoleapp.dao.GroupsDao;
import ua.foxminded.schoolconsoleapp.entity.Group;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public class GroupDaoImpl extends AbstractDaoImpl<Group> implements GroupsDao {
    private static final String PROPERTY_GROUP_ADD = "INSERT INTO schedule.groups(group_name) VALUES (?)";
    private static final String PROPERTY_GROUP_GET_BY_ID = "SELECT group_id, group_name FROM schedule.groups WHERE group_id = ?";
    private static final String PROPERTY_GROUP_GET_ALL = "SELECT * FROM schedule.groups limit ? offset ?";
    private static final String PROPERTY_GROUP_UPDATE = "UPDATE schedule.groups SET group_name = ? WHERE group_id = ?";
    private static final String PROPERTY_GROUP_DELETE = "DELETE FROM schedule.groups WHERE group_id = ?";
    private static final String PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT = "SELECT groups.group_id, groups.group_name, COUNT(student_id) "
	    + "FROM schedule.groups " + "LEFT JOIN schedule.students ON groups.group_id = students.group_id "
	    + "GROUP BY groups.group_id, groups.group_name " + "HAVING COUNT(student_id) <= ?";

    private static final String FIELD_GROUP_ID = "group_id";
    private static final String FIELD_GROUP_NAME = "group_name";
    private static final String MESSAGE_EXCEPTION_GET_ALL = "Can't get groups";

    public GroupDaoImpl(ConnectionProvider connector) {
	super(connector, PROPERTY_GROUP_ADD, PROPERTY_GROUP_GET_BY_ID, PROPERTY_GROUP_GET_ALL, PROPERTY_GROUP_UPDATE,
		PROPERTY_GROUP_DELETE);
    }

    @Override
    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) throws DaosException {
	List<Group> groups = new ArrayList<>();
	try (Connection connection = connectionProvider.getConnection();
		PreparedStatement statement = connection.prepareStatement(PROPERTY_FIND_GROUPS_LESS_STUDENT_COUNT)) {
	    statement.setInt(1, studentCount);
	    try (ResultSet resultSet = statement.executeQuery()) {
		while (resultSet.next()) {
		    Group group = Group.builder().withGroupId(resultSet.getInt(FIELD_GROUP_ID))
			    .withGroupName(resultSet.getString(FIELD_GROUP_NAME)).build();

		    groups.add(group);
		}
	    }
	} catch (SQLException e) {
	    throw new DaosException(MESSAGE_EXCEPTION_GET_ALL, e);
	}
	return groups;
    }

    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
	Group group = Group.builder().withGroupId(resultSet.getInt(FIELD_GROUP_ID))
		.withGroupName(resultSet.getString(FIELD_GROUP_NAME)).build();

	return group;
    }

    @Override
    protected void insertSave(PreparedStatement statement, Group entity) throws SQLException {
	statement.setString(1, entity.getGroupName());
    }

    @Override
    protected void insertUpdate(PreparedStatement statement, Group entity) throws SQLException {
	statement.setString(1, entity.getGroupName());
	statement.setInt(2, entity.getGroupId());
    }
}
