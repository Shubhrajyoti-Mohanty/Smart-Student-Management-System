package com.sms.util;

import com.sms.model.Student;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A tiny utility class to handle simple JSON serialization/deserialization 
 * since we are restricting external dependencies (like Jackson or Gson).
 */
public class JsonUtil {

    /**
     * Converts a single Student object to a JSON string.
     */
    public static String studentToJson(Student student) {
        if (student == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":").append(student.getId()).append(",");
        sb.append("\"name\":\"").append(escapeJson(student.getName())).append("\",");
        sb.append("\"age\":").append(student.getAge()).append(",");
        if (student.getGender() != null) {
            sb.append("\"gender\":\"").append(escapeJson(student.getGender())).append("\",");
        }
        sb.append("\"course\":\"").append(escapeJson(student.getCourse())).append("\",");
        sb.append("\"attendance\":").append(student.getAttendance()).append(",");
        sb.append("\"marks\":").append(student.getMarks()).append(",");
        sb.append("\"grade\":\"").append(student.getGrade()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts a List of Students to a JSON array string.
     */
    public static String studentListToJson(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < students.size(); i++) {
            sb.append(studentToJson(students.get(i)));
            if (i < students.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Parses a simple flat JSON string into a Map of String, String.
     * Only works with flat, non-nested objects, which is sufficient for this mini-project.
     */
    public static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return map;
        }

        // Remove curly braces
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        // Split by comma
        // Note: This naive split fails if there are commas inside string values. 
        // For a robust system, use a library, but this works for basic names/courses without commas.
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();
                // Remove quotes around values
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    public static String successResponse(String message) {
        return "{\"status\":\"success\", \"message\":\"" + escapeJson(message) + "\"}";
    }

    public static String errorResponse(String message) {
        return "{\"status\":\"error\", \"message\":\"" + escapeJson(message) + "\"}";
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
