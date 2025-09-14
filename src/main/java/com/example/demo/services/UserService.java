
package com.example.demo.services;

import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(String uid);
    User updateUser(String uid, User user);
    void deleteUser(String uid);
    List<User> getAllUsers();
}
