package ua.foxminded.entity;

import java.util.Objects;

public class Group {
    private int groupId;
    private String groupName;
    
    public Group() {
	
    }

    public Group(int groupId, String groupName) {
	this.groupId = groupId;
	this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {	
	return Objects.hash(groupId, groupName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Group other = (Group) obj;
	
	return Objects.equals(groupId, other.groupId) &&
		Objects.equals(groupName, other.groupName); 
    }

    @Override
    public String toString() {
	return "Group [groupId=" + groupId + '\'' + ", groupName=" + groupName + "]";
    }
    
}
