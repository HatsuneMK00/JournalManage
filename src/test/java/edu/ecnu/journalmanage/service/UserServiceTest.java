package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    /**
     * 测试用户登录
     */
    @Test
    void login() {
        User admin = userService.login("admin", "123456");
        assertNotNull(admin);
        assertEquals(Role.admin, admin.getRole());

        // return null if either username or password is wrong
        admin = userService.login("admin", "12345678");
        assertNull(admin);
        User user = userService.login("not exist", "123456");
        assertNull(user);
    }

    /**
     * 测试用户注册
     */
    @Test
    void register() {
        // Author has a default value of true for field valid
        User user = new User();
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.author);
        String err = userService.register(user);
        assertNull(err);
        assertEquals(Role.author, user.getRole());
        assertTrue(user.getId() > 0);
        assertTrue(user.isValid());

        // Other roles have a default value of false for field valid
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.editor);
        err = userService.register(user);
        assertNull(err);
        assertEquals(Role.editor, user.getRole());
        assertTrue(user.getId() > 0);
        assertFalse(user.isValid());

        // more than one chief editor can register but only one is valid
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.chiefEditor);
        err = userService.register(user);
        assertNull(err);
        assertEquals(Role.chiefEditor, user.getRole());
        assertTrue(user.getId() > 0);
        assertFalse(user.isValid());
    }

    /**
     * 测试完善用户信息
     */
    @Test
    void completeUserInfo() {
        User user = userService.login("test", "123456");
        assertNotNull(user);
        assertEquals(Role.author, user.getRole());

        String oldEmail = user.getEmail();
        String oldPassword = user.getPassword();
        int oldId = user.getId();
        boolean oldValid = user.isValid();
        user.setPassword("12345678");
        user.setEmail(System.currentTimeMillis() + "@ecnu.edu.cn");
        String err = userService.completeUserInfo(user);
        assertNull(err);
        assertEquals(Role.author, user.getRole());
        assertNotEquals(oldEmail, user.getEmail());
        assertNotEquals(oldPassword, user.getPassword());
        assertEquals(oldId, user.getId());
        assertEquals(oldValid, user.isValid());
    }

    @Test
    void registerDuplicatedUser() {
        User user = new User();
        user.setName("test" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.author);
        String err = userService.register(user);
        assertNull(err);
        assertEquals(Role.author, user.getRole());
        assertTrue(user.getId() > 0);
        assertTrue(user.isValid());

        err = userService.register(user);
        assertNotNull(err);
    }
}
