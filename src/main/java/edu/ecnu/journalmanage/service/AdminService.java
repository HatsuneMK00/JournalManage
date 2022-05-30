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

    public String changeUserRole(@NotNull User user) {
        if (user.getId() == null) {
            return "User id is null";
        }
        if (user.getRole() == null) {
            return "User role is null";
        }
        if (user.getRole() == Role.ADMIN) {
            return "User role cannot be admin";
        }
        if (user.getRole() == Role.CHIEF_EDITOR) {
            // fixme potential concurrency issue
            int chiefEditorCount = userMapper.getChiefEditorCount();
            if (chiefEditorCount >= 1) {
                return "Chief editor count is already 1";
            }
        }
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
                case AUTHOR:
                    map.get("authors").add(user);
                    break;
                case EDITOR:
                    map.get("editors").add(user);
                    break;
                case CHIEF_EDITOR:
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
