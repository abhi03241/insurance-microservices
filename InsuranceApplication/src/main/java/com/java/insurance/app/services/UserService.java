package com.java.insurance.app.services;


import com.java.insurance.app.models.User;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Integer id);

    User createUser(@Validated User user);

    User updateUser(User user);

    void deleteUser(int userId);

    User getUserByEmail(String email);
}
