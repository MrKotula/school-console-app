package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DAOException;

public interface StudentDAO extends DAO<Student, Integer> {
    void addStudentCourse(int studentId, int courseId) throws DAOException;
    
    void removeStudentFromCourse(int studentId, int courseId) throws DAOException;
    
    List<Student> getStudentsWithCourseName(String courseName) throws DAOException;
}
