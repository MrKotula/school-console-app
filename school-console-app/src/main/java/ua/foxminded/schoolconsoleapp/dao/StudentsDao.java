package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Student;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public interface StudentsDao extends Daos<Student, Integer> {
    void addStudentCourse(int studentId, int courseId) throws DaosException;

    void removeStudentFromCourse(int studentId, int courseId) throws DaosException;

    List<Student> getStudentsWithCourseName(String courseName) throws DaosException;
}
