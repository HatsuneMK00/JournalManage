package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final
    UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.isValid() && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public String register(User user) {
        String err;
        if (user.getRole() == null) {
            return "User role cannot be null";
        }
        if (user.getRole() == Role.admin) {
            return "Admin cannot register";
        }
        user.setValid(user.getRole() == Role.author);
        int affected;
        try {
            affected = userMapper.addUser(user);
        } catch (Exception e) {
            return "Register failed with exception: " + e.getMessage();
        }
        return affected == 1 ? null : "Register failed";
    }

    public String completeUserInfo(@NotNull User user) {
        if (user.getId() == null) {
            return "User id is null";
        }
        return userMapper.updateUser(user) == 1 ? null : "Update user failed";
    }

}
