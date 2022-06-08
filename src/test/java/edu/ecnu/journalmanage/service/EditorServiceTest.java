package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageInfo;
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

    @Test
    void getToReviewArticleByEditor() {
        PageInfo<Article> articlesPage = editorService.getToReviewArticlesPaged(33, 1, 2);
        System.out.println(articlesPage);
        assertTrue(articlesPage.getList().size() > 0);
        assertTrue(articlesPage.isHasNextPage());
    }

    @Test
    void getUnbindArticle() {
        PageInfo<Article> paged = editorService.getAllUnbindArticlesPaged(1, 1);
        assertTrue(paged.getList().size() > 0);
        assertTrue(paged.isHasNextPage());
    }
}
