package com.sms;

import com.sms.handler.AuthHandler;
import com.sms.handler.StaticFileHandler;
import com.sms.handler.StudentHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            // Create HTTP Server on port 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Create context for API endpoints
            server.createContext("/api/login", new AuthHandler());
            server.createContext("/api/students", new StudentHandler());

            // Create context for serving static frontend files
            // Assumes the application is run with working directory = project root
            server.createContext("/", new StaticFileHandler("frontend"));

            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Server started on port 8080");
            System.out.println("Open http://localhost:8080/ in your browser.");
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
