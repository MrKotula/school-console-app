package ua.foxminded.schoolconsoleapp.entity;

import java.util.Objects;

public class Course {
    private Integer courseId;
    private String courseName;
    private String courseDescription;
    
    public Course() {

    }
    
    public Course(String courseName, String courseDescription) {
	this.courseName = courseName;
	this.courseDescription = courseDescription;
    }
    
    public Course(Integer courseId, String courseName, String courseDescription) {
	this.courseId = courseId;
   	this.courseName = courseName;
   	this.courseDescription = courseDescription;
       }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    @Override
    public int hashCode() {
	return Objects.hash(courseDescription, courseId, courseName);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Course other = (Course) obj;
	
	return 	Objects.equals(courseDescription, other.courseDescription) &&
		Objects.equals(courseId, other.courseId) &&
		Objects.equals(courseName, other.courseName);
    }

    @Override
    public String toString() {
	return "Course [courseId=" + courseId + '\'' + ", courseName=" + courseName + '\'' + ", courseDescription="
		+ courseDescription + "]";
    }
}
