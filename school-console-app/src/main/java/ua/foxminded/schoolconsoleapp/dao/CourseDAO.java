package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

public interface CourseDAO extends DAO<Course, Integer> {
    List<Course> getCoursesForStudentId(int studentId) throws DAOException;
    
    List<Course> getCoursesMissingForStudentId(int studentId) throws DAOException;
}
