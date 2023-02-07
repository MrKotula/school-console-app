package ua.foxminded.schoolconsoleapp.entity;

import java.util.Objects;

public class Student {
    private final Integer studentId;
    private final Integer groupId;
    private final String firstName;
    private final String lastName;
    
    public Student(Builder builder) {
	this.studentId = builder.studentId;
	this.groupId = builder.groupId;
	this.firstName = builder.firstName;
	this.lastName = builder.lastName;
    }
    
    public static Builder builder() {
	return new Builder();
    }

    public Integer getStudentId() {
        return studentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(firstName, groupId, lastName, studentId);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Student other = (Student) obj;
	
	return 	Objects.equals(firstName, other.firstName) && 
		Objects.equals(groupId, other.groupId) && 
		Objects.equals(studentId, other.studentId) && 
		Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
	return "Student [studentId=" + studentId + '\'' + ", groupId=" + groupId + '\'' +
		", firstName=" + firstName + '\'' + ", lastName=" + lastName + "]";
    }
    
    public static class Builder {
	private Integer studentId;
	private Integer groupId;
	private String firstName;
	private String lastName;
	
	private Builder() {
	    
	}
	
	public Builder withStudentId(Integer studentId) {
	    this.studentId = studentId;
	    return this;
	}
	
	public Builder withGroupId(Integer groupId) {
	    this.groupId = groupId;
	    return this;
	}
	
	public Builder withFirstName(String firstName) {
	    this.firstName = firstName;
	    return this;
	}
	
	public Builder withLastName(String lastName) {
	    this.lastName = lastName;
	    return this;
	}
	
	public Student build() {
	    return new Student(this);
	}
    }
}
