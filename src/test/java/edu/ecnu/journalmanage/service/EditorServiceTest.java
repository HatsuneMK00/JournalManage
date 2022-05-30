package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.Role;
import edu.ecnu.journalmanage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EditorServiceTest {
    @Autowired
    EditorService editorService;
    @Autowired
    UserMapper userMapper;

    /**
     * 测试获取代审稿件
     */
    @Test
    void getToReviewArticle() {
        User user = new User();
        user.setName("editor" + UUID.randomUUID());
        user.setPassword("123456");
        user.setRole(Role.editor);
        userMapper.addUser(user);

        List<Article> toReviewArticles = editorService.getToReviewArticles(user.getId());
        assertTrue(toReviewArticles.size() > 0);
    }
}
