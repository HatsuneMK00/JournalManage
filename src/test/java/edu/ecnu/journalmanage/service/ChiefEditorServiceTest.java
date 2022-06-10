package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChiefEditorServiceTest {
    @Autowired
    ChiefEditorService chiefEditorService;

    @Test
    void getToReviewArticlesPaged() {
        PageInfo<Article> articlesPage = chiefEditorService.getToReviewArticlesPaged(16, 1, 5);
        System.out.println(articlesPage);
        assertTrue(articlesPage.getList().size() > 0);
    }

    @Test
    void getReviewedArticlesPaged() {
        PageInfo<Article> articlesPage = chiefEditorService.getReviewedArticlesPaged(16, 1, 5);
        System.out.println(articlesPage);
        assertTrue(articlesPage.getList().size() > 0);
    }
}
