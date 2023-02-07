package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import ua.foxminded.schoolconsoleapp.entity.Course;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public interface CoursesDao extends Daos<Course, Integer> {
    List<Course> getCoursesForStudentId(int studentId) throws DaosException;

    List<Course> getCoursesMissingForStudentId(int studentId) throws DaosException;
}
