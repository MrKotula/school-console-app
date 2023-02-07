package ua.foxminded.schoolconsoleapp.entity;

import java.util.Objects;

public class Student {
    private Integer studentId;
    private Integer groupId;
    private String firstName;
    private String lastName;
    
    public Student() {
	
    }
    
    public Student(Integer studentId, Integer groupId, String firstName, String lastName) {
	this.studentId = studentId;
	this.groupId = groupId;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    public Student(Integer studentId) {
	this.studentId = studentId;
    }
    
    public Student(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
    }
    
    public Student(Integer groupId, String firstName, String lastName) {
	this.groupId = groupId;
	this.firstName = firstName;
	this.lastName = lastName;
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

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
