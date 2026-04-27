package com.sms.handler;

import com.sms.service.AuthService;
import com.sms.util.CorsUtil;
import com.sms.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class AuthHandler implements HttpHandler {

    private AuthService authService;

    public AuthHandler() {
        this.authService = new AuthService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        // Handle CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            CorsUtil.addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equalsIgnoreCase(method)) {
            handleLogin(exchange);
        } else {
            CorsUtil.sendResponse(exchange, 405, JsonUtil.errorResponse("Method not allowed"));
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Map<String, String> payload = JsonUtil.parseSimpleJson(sb.toString());
        String username = payload.get("username");
        String password = payload.get("password");

        if (authService.login(username, password)) {
            // For a simple project, we'll just return a success token or flag.
            // In a real project, we would generate a JWT or Session ID.
            String jsonResponse = "{\"status\":\"success\", \"token\":\"simulated-token-123\", \"message\":\"Login successful\"}";
            CorsUtil.sendResponse(exchange, 200, jsonResponse);
        } else {
            CorsUtil.sendResponse(exchange, 401, JsonUtil.errorResponse("Invalid username or password"));
        }
    }
}
