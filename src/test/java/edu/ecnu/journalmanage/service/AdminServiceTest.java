package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AdminService adminService;


    /**
     * 测试管理员批量删除Editor
     */
    @Test
    void deleteEditors() {
        generateTempUser(Role.editor);

        List<User> editors = adminService.getAllEditors();
        for (User editor : editors) {
            String err = adminService.deleteUser(editor.getId());
            // err is null means delete successfully
            assertNull(err);
        }

        assertEquals(0, adminService.getAllEditors().size());
    }

    /**
     * 测试管理员批量删除Expert
     */
    @Test
    void deleteExperts() {
        generateTempUser(Role.expert);

        List<User> editors = adminService.getAllExperts();
        for (User editor : editors) {
            String err = adminService.deleteUser(editor.getId());
            // err is null means delete successfully
            assertNull(err);
        }

        assertEquals(0, adminService.getAllExperts().size());
    }

    private void generateTempUser(Role role) {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            String name = "unit test" + UUID.randomUUID();
            user.setName(name);
            user.setPassword("123456");
            user.setRole(role);
            user.setValid(true);
            user.setEmail("test@test.com");
            users.add(user);
        }
        for (User user : users) {
            userMapper.addUser(user);
        }
    }

    /**
     * 测试管理员审核用户
     */
    @Test
    void auditUser() {
        User user = new User();
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.editor);
        user.setValid(false);
        user.setEmail("test@test.com");
        userMapper.addUser(user);
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.expert);
        user.setValid(false);
        user.setEmail("test@test.com");
        userMapper.addUser(user);
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.chiefEditor);
        user.setValid(false);
        user.setEmail("test@test.com");
        userMapper.addUser(user);

        Map<String, List<User>> invalidUsers = adminService.getAllInvalidUsers();
        List<User> invalidEditors = invalidUsers.get("editors");
        List<User> invalidExperts = invalidUsers.get("experts");
        List<User> invalidChiefEditors = invalidUsers.get("chiefEditors");
        assertAll(
                () -> assertTrue(invalidEditors.size() > 0),
                () -> assertTrue(invalidExperts.size() > 0),
                () -> assertTrue(invalidChiefEditors.size() > 0)
        );

        for (User invalidEditor : invalidEditors) {
            String err = adminService.validateUser(invalidEditor.getId(), Role.editor);
            assertNull(err);
        }
        for (User invalidExpert : invalidExperts) {
            String err = adminService.validateUser(invalidExpert.getId(), Role.expert);
            assertNull(err);
        }
        for (User invalidChiefEditor : invalidChiefEditors) {
            // cannot have more than one chief editor
            String err = adminService.validateUser(invalidChiefEditor.getId(), Role.chiefEditor);
            assertNotNull(err);
        }

        invalidUsers = adminService.getAllInvalidUsers();
        assertEquals(0, invalidUsers.get("editors").size());
        assertEquals(0, invalidUsers.get("experts").size());
        assertTrue(invalidUsers.get("chiefEditors").size() > 0);
    }
}
