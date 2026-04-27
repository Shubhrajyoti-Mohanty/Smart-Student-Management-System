package com.sms.handler;

import com.sms.model.Student;
import com.sms.service.StudentService;
import com.sms.util.CorsUtil;
import com.sms.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class StudentHandler implements HttpHandler {

    private StudentService studentService;

    public StudentHandler() {
        this.studentService = new StudentService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            CorsUtil.addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    CorsUtil.sendResponse(exchange, 405, JsonUtil.errorResponse("Method not allowed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            CorsUtil.sendResponse(exchange, 500, JsonUtil.errorResponse("Internal Server Error: " + e.getMessage()));
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("id=")) {
            // Get single student
            int id = Integer.parseInt(query.split("=")[1]);
            Student student = studentService.getStudent(id);
            if (student != null) {
                CorsUtil.sendResponse(exchange, 200, JsonUtil.studentToJson(student));
            } else {
                CorsUtil.sendResponse(exchange, 404, JsonUtil.errorResponse("Student not found"));
            }
        } else {
            // Get all students
            List<Student> students = studentService.getAllStudents();
            CorsUtil.sendResponse(exchange, 200, JsonUtil.studentListToJson(students));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> payload = parseRequestBody(exchange);
        Student student = mapToStudent(payload);
        
        if (studentService.addStudent(student)) {
            CorsUtil.sendResponse(exchange, 201, JsonUtil.successResponse("Student added successfully"));
        } else {
            CorsUtil.sendResponse(exchange, 400, JsonUtil.errorResponse("Failed to add student. Validation error."));
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        Map<String, String> payload = parseRequestBody(exchange);
        Student student = mapToStudent(payload);
        
        // Ensure ID is present for update
        if (payload.containsKey("id") && !payload.get("id").isEmpty()) {
            student.setId(Integer.parseInt(payload.get("id")));
        } else {
            CorsUtil.sendResponse(exchange, 400, JsonUtil.errorResponse("Student ID required for update"));
            return;
        }

        if (studentService.updateStudent(student)) {
            CorsUtil.sendResponse(exchange, 200, JsonUtil.successResponse("Student updated successfully"));
        } else {
            CorsUtil.sendResponse(exchange, 400, JsonUtil.errorResponse("Failed to update student."));
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            if (studentService.deleteStudent(id)) {
                CorsUtil.sendResponse(exchange, 200, JsonUtil.successResponse("Student deleted successfully"));
            } else {
                CorsUtil.sendResponse(exchange, 400, JsonUtil.errorResponse("Failed to delete student"));
            }
        } else {
            CorsUtil.sendResponse(exchange, 400, JsonUtil.errorResponse("Student ID required for deletion"));
        }
    }

    private Map<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return JsonUtil.parseSimpleJson(sb.toString());
    }

    private Student mapToStudent(Map<String, String> map) {
        Student student = new Student();
        student.setName(map.get("name"));
        student.setCourse(map.get("course"));
        student.setGender(map.get("gender"));
        
        if (map.containsKey("age")) student.setAge(Integer.parseInt(map.get("age")));
        if (map.containsKey("attendance")) student.setAttendance(Double.parseDouble(map.get("attendance")));
        if (map.containsKey("marks")) student.setMarks(Double.parseDouble(map.get("marks")));
        
        return student;
    }
}
