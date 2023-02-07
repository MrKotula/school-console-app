package ua.foxminded.schoolconsoleapp.menu;

import ua.foxminded.schoolconsoleapp.exception.DaosException;

public interface MenuStarter {
    void startMenu() throws DaosException;
    
    void findAllGroupsWithStudentCount() throws DaosException;
    
    void findAllStudentsToCourseName() throws DaosException;
    
    void addStudent() throws DaosException;
    
    void deleteStudentById() throws DaosException;
    
    void addStudentToCourse() throws DaosException;
    
    void removeStudentFromCourse() throws DaosException;   
}
