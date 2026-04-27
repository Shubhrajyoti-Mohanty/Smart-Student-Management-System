package com.sms.service;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import java.util.List;

public class StudentService {

    private StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
    
    public Student getStudent(int id) {
        return studentDAO.getStudentById(id);
    }

    public boolean addStudent(Student student) {
        if (!validateStudent(student)) {
            return false;
        }
        student.setGrade(calculateGrade(student.getMarks()));
        return studentDAO.addStudent(student);
    }

    public boolean updateStudent(Student student) {
        if (!validateStudent(student)) {
            return false;
        }
        student.setGrade(calculateGrade(student.getMarks()));
        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    /**
     * Calculates the grade based on the marks.
     * 90+ → A
     * 75–89 → B
     * 60–74 → C
     * <60 → D
     */
    private String calculateGrade(double marks) {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        return "D";
    }

    /**
     * Validates student data.
     */
    private boolean validateStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) return false;
        if (student.getGender() == null || student.getGender().trim().isEmpty()) return false;
        if (student.getCourse() == null || student.getCourse().trim().isEmpty()) return false;
        if (student.getAge() <= 0) return false;
        if (student.getAttendance() < 0 || student.getAttendance() > 100) return false;
        if (student.getMarks() < 0 || student.getMarks() > 100) return false;
        return true;
    }
}
