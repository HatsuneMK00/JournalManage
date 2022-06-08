package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        map.put("experts", new ArrayList<>());
        map.put("editors", new ArrayList<>());
        map.put("chiefEditors", new ArrayList<>());
        for (User user : users) {
            switch (user.getRole()) {
                case expert:
                    map.get("experts").add(user);
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

    public PageInfo<User> getAllInvalidUsersPaged(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.getAllInvalidUsers();
        return new PageInfo<>(users);
    }

    public List<User> getAllEditors() {
        return userMapper.getUsersByRole(Role.editor);
    }

    public PageInfo<User> getAllEditorsPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.getAllEditors());
    }

    public List<User> getAllExperts() {
        return userMapper.getUsersByRole(Role.expert);
    }

    public PageInfo<User> getAllExpertsPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.getAllExperts());
    }

    public String validateUser(int userId, Role role) {
        if (role == Role.chiefEditor) {
            // fixme potential concurrency issue
            int chiefEditorCount = userMapper.getChiefEditorCount();
            if (chiefEditorCount >= 1) {
                return "Chief editor count is already 1";
            }
        }
        return userMapper.updateUserValid(userId, true) == 1 ? null : "Validate user failed";
    }

    public String deleteUser(int userId) {
        return userMapper.deleteUser(userId) == 1 ? null : "Delete user failed";
    }
}
