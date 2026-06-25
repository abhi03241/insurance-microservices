package com.java.insurance.app.services;

import com.java.insurance.app.models.User;

public interface AdminService {

    User createAdmin(User admin);

    User createUnderwriter(User underwriter);

    void deleteUnderwriter(int underwriterId);

    void deleteAdmin(int adminId);

}
