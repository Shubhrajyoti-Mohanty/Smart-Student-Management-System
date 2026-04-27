package com.sms.service;

import com.sms.dao.AdminDAO;
import com.sms.model.Admin;

public class AuthService {

    private AdminDAO adminDAO;

    public AuthService() {
        this.adminDAO = new AdminDAO();
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        Admin admin = adminDAO.authenticate(username, password);
        return admin != null;
    }
}
