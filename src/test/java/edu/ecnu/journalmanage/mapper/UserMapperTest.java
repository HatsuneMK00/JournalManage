package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void getUserById() {
        User user = userMapper.getUserById(1);
        // assert userMapper.getUser(1).getUserName().equals("admin");
        assertAll(
                () -> assertEquals("admin", user.getName()),
                () -> assertEquals(Role.ADMIN, user.getRole()),
                () -> assertEquals(1, user.getId()),
                () -> assertTrue(user.isValid())
        );
    }

    @Test
    void addUser() {
        User user = new User();
        String name = "test" + System.currentTimeMillis();
        user.setName(name);
        user.setPassword("123456");
        user.setRole(Role.AUTHOR);
        user.setValid(true);
        user.setEmail("test@test.com");
        userMapper.addUser(user);
        User user1 = userMapper.getUserById(user.getId());
        assertEquals(user1.getName(), name);
        assertEquals(user1.getPassword(), "123456");
        assertEquals(user1.getRole(), Role.AUTHOR);
        assertEquals(user1.getEmail(), "test@test.com");
    }

    @Test
    void addUserWithResearchArea() {
        User user = new User();
        String name = "test" + System.currentTimeMillis();
        user.setName(name);
        user.setPassword("123456");
        user.setRole(Role.AUTHOR);
        user.setValid(true);
        user.setResearchArea("test area");
        int affected = userMapper.addUser(user);
        User user1 = userMapper.getUserById(user.getId());
        assertEquals(affected, 1);
        assertEquals(user1.getName(), name);
        assertEquals(user1.getResearchArea(), "test area");
    }

    @Test
    void getUsersByRole() {
        // assert users is greater than 1
        assertTrue(userMapper.getUsersByRole(Role.AUTHOR).size() > 1);
    }

    @Test
    void deleteUser() {
        User user = new User();
        String name = "test" + System.currentTimeMillis();
        user.setName(name);
        user.setPassword("123456");
        user.setRole(Role.AUTHOR);
        user.setValid(true);
        user.setResearchArea("test area");
        int affected = userMapper.addUser(user);
        int deleteAffected = userMapper.deleteUser(user.getId());
        assertEquals(affected, 1);
        assertEquals(deleteAffected, 1);
    }

    @Test
    void deleteNotExistedUser() {
        assertEquals(userMapper.deleteUser(0), 0);
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setName("update test");
        user.setPassword("12345678");
        user.setId(2);
        user.setRole(Role.AUTHOR);
        user.setResearchArea("test area update");
        userMapper.updateUser(user);
        User user1 = userMapper.getUserById(user.getId());
        assertEquals(user1.getName(), "update test");
        assertEquals(user1.getResearchArea(), "test area update");
        assertEquals(user1.getPassword(), "12345678");
    }
}
