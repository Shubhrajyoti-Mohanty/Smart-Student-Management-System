USE sms;

-- Drop tables if they exist to allow clean recreation
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS admin_users;

-- Admin Users Table
CREATE TABLE admin_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students Table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL DEFAULT 'Unknown',
    course VARCHAR(100) NOT NULL,
    attendance DOUBLE NOT NULL,
    marks DOUBLE NOT NULL,
    grade VARCHAR(2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Default Admin (Password should be hashed in a real app, but plain text for this simple mini-project)
INSERT INTO admin_users (username, password) VALUES ('admin', 'admin123');

-- Insert Sample Students
INSERT INTO students (name, age, gender, course, attendance, marks, grade) VALUES
('John Doe', 20, 'Male', 'Computer Science', 85.5, 92.0, 'A'),
('Jane Smith', 22, 'Female', 'Information Technology', 90.0, 88.0, 'B'),
('Michael Johnson', 21, 'Male', 'Software Engineering', 72.0, 65.0, 'C'),
('Emily Davis', 23, 'Female', 'Data Science', 95.0, 98.0, 'A'),
('Chris Brown', 19, 'Male', 'Computer Science', 55.0, 45.0, 'D');
