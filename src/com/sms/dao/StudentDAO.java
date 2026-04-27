package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Add Student
    public boolean addStudent(Student student) {
        String query = "INSERT INTO students (name, age, gender, course, attendance, marks, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getCourse());
            pstmt.setDouble(5, student.getAttendance());
            pstmt.setDouble(6, student.getMarks());
            pstmt.setString(7, student.getGrade());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // Get All Students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                String gender = null;
                try {
                    gender = rs.getString("gender");
                } catch (SQLException e) {
                    // Column might not exist, ignore
                }
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    gender,
                    rs.getString("course"),
                    rs.getDouble("attendance"),
                    rs.getDouble("marks"),
                    rs.getString("grade")
                );
                students.add(student);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        
        return students;
    }

    // Get Student by ID
    public Student getStudentById(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String gender = null;
                    try {
                        gender = rs.getString("gender");
                    } catch (SQLException e) {
                        // Column might not exist, ignore
                    }
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        gender,
                        rs.getString("course"),
                        rs.getDouble("attendance"),
                        rs.getDouble("marks"),
                        rs.getString("grade")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
        }
        return null;
    }

    // Update Student
    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET name = ?, age = ?, gender = ?, course = ?, attendance = ?, marks = ?, grade = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getCourse());
            pstmt.setDouble(5, student.getAttendance());
            pstmt.setDouble(6, student.getMarks());
            pstmt.setString(7, student.getGrade());
            pstmt.setInt(8, student.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    // Delete Student
    public boolean deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
}
