package ua.foxminded.schoolconsoleapp.entity;

import java.util.Objects;

public class Group {
    private Integer groupId;
    private final String groupName;
    
    public Group(Builder builder) {
	this.groupId = builder.groupId;
	this.groupName = builder.groupName;
    }
    
    public static Builder builder() {
	return new Builder();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {	
	return Objects.hash(groupId, groupName);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Group other = (Group) obj;
	
	return 	Objects.equals(groupId, other.groupId) &&
		Objects.equals(groupName, other.groupName); 
    }

    @Override
    public String toString() {
	return "Group [groupId=" + groupId + '\'' + ", groupName=" + groupName + "]";
    }
    
    public static class Builder {
	private Integer groupId;
	private String groupName;
	
	private Builder() {
	    
	}
	
	public Builder withGroupId(Integer groupId) {
	    this.groupId = groupId;
	    return this;
	}
	
	public Builder withGroupName(String groupName) {
	    this.groupName = groupName;
	    return this;
	}
	
	public Group build() {
	    return new Group(this);
	}
    }
}
