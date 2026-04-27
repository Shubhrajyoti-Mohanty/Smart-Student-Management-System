# Smart Student Management System

A complete, production-quality mini project for managing student records. Built to demonstrate a full-stack Java application without the use of heavy external frameworks like Spring Boot or heavy web containers like Tomcat. 

This project uses Java's built-in `HttpServer` to serve both a RESTful API and a static HTML/CSS/Vanilla JS frontend.

## 🌟 Features

- **Full CRUD Operations**: Add, View, Update, and Delete student records.
- **Admin Authentication**: Secure login system validated against a MySQL database.
- **Automated Business Logic**: Calculates student grades automatically based on their marks (e.g., 90+ = A, 75-89 = B).
- **Data Validation**: Enforces strict data integrity on both the client side (JavaScript) and server side (Java).
- **Premium User Interface**: Clean, modern dashboard using CSS3 features like Glassmorphism, flexbox, and grid layouts.
- **Zero External Frameworks**: Pure Core Java backend (only the MySQL JDBC driver is used).

## 🛠 Tech Stack

**Backend**
- Core Java (JDK 8+)
- `com.sun.net.httpserver.HttpServer` (Built-in Java HTTP Server)
- JDBC API
- Custom lightweight JSON serialization utility

**Frontend**
- HTML5
- CSS3 (Vanilla, Custom UI components)
- JavaScript (Vanilla, Fetch API)

**Database**
- MySQL

## 📋 Prerequisites

Before you begin, ensure you have met the following requirements:
* **Java Development Kit (JDK)** installed and added to your system PATH.
* **MySQL Server** installed and running locally.
* `mysql-connector-j` driver jar file downloaded and placed in the root of the project.

## 🚀 Setup & Installation

1. **Database Setup**
   * Open your MySQL client.
   * Create a database named `sms`: `CREATE DATABASE sms;`
   * Import the provided database schema:
     ```bash
     mysql -u root -p sms < sql/schema.sql
     ```

2. **Configure Database Connection**
   * If your MySQL username is not `root` or your password is not `root`, update the credentials in `src/com/sms/util/DBConnection.java`:
     ```java
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

3. **Compile and Run**
   * Navigate to the project root directory in your terminal or command prompt.
   * A convenience batch script is provided for Windows users. Run:
     ```cmd
     .\run.bat
     ```
   * *This script will automatically compile all `.java` files into a `/bin` directory and start the server.*

## 💻 Usage

1. Open your web browser and navigate to `http://localhost:8080`
2. **Login Credentials**:
   * **Username:** `admin`
   * **Password:** `admin123`
3. Explore the dashboard to view metrics, add new students, and manage existing records in the tabular view.

## 📁 Folder Structure

```text
Student Management System/
├── .gitignore
├── README.md
├── run.bat                       # Build & execution script
├── mysql-connector-j-9.6.0.jar   # JDBC Driver
├── sql/
│   └── schema.sql                # DB Tables & sample data
├── src/com/sms/
│   ├── Main.java                 # Entry point & Server config
│   ├── model/                    # POJOs (Student, Admin)
│   ├── dao/                      # Database Operations
│   ├── service/                  # Business Logic (Grades, Validation)
│   ├── handler/                  # HTTP Endpoints (API & Static files)
│   └── util/                     # Utilities (DBConnection, JsonUtil, CorsUtil)
└── frontend/                     # Static Web Assets
    ├── login.html
    ├── dashboard.html
    ├── add-student.html
    ├── view-students.html
    ├── css/style.css
    └── js/script.js
```
