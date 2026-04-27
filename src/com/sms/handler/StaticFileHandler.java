package com.sms.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticFileHandler implements HttpHandler {

    private final String baseDir;

    public StaticFileHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // Default to dashboard.html or login.html if root is requested
        if ("/".equals(path)) {
            path = "/login.html";
        }

        File file = new File(baseDir, path).getCanonicalFile();
        
        // Simple security check to prevent directory traversal
        if (!file.getPath().startsWith(new File(baseDir).getCanonicalPath())) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        if (!file.exists() || file.isDirectory()) {
            String response = "404 (Not Found)\n";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType != null) {
            exchange.getResponseHeaders().add("Content-Type", mimeType);
        } else if (file.getName().endsWith(".js")) {
            exchange.getResponseHeaders().add("Content-Type", "application/javascript");
        } else if (file.getName().endsWith(".css")) {
            exchange.getResponseHeaders().add("Content-Type", "text/css");
        }

        exchange.sendResponseHeaders(200, file.length());
        try (OutputStream os = exchange.getResponseBody(); FileInputStream fs = new FileInputStream(file)) {
            final byte[] buffer = new byte[4096];
            int count;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }
        }
    }
}
