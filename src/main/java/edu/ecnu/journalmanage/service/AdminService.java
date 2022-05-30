package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    final
    UserMapper userMapper;

    public AdminService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public String changeUserRole(int userId, @NotNull Role role) {
        if (role == Role.admin) {
            return "User role cannot be admin";
        }
        if (role == Role.chiefEditor) {
            // fixme potential concurrency issue
            int chiefEditorCount = userMapper.getChiefEditorCount();
            if (chiefEditorCount >= 1) {
                return "Chief editor count is already 1";
            }
        }
        User user = new User();
        user.setId(userId);
        user.setRole(role);
        return userMapper.changeUserRole(user) == 1 ? null : "Update user failed";
    }

    public Map<String, List<User>> getAllInvalidUsers() {
        List<User> users = userMapper.getAllInvalidUsers();
        HashMap<String, List<User>> map = new HashMap<>();
        map.put("authors", new ArrayList<>());
        map.put("editors", new ArrayList<>());
        map.put("chiefEditors", new ArrayList<>());
        for (User user : users) {
            switch (user.getRole()) {
                case author:
                    map.get("authors").add(user);
                    break;
                case editor:
                    map.get("editors").add(user);
                    break;
                case chiefEditor:
                    map.get("chiefEditors").add(user);
                    break;
            }
        }
        return map;
    }

    public String deleteUser(int userId) {
        return userMapper.deleteUser(userId) == 1 ? null : "Delete user failed";
    }
}
