package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.*;
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

    @Test
    void getToReviewArticleByEditor() {
        PageInfo<Article> articlesPage = editorService.getToReviewArticlesPaged(33, 1, 5);
        System.out.println(articlesPage);
        assertTrue(articlesPage.getList().size() > 0);
    }

    @Test
    void getUnbindArticle() {
        PageInfo<Article> paged = editorService.getAllUnbindArticlesPaged(1, 5);
        assertTrue(paged.getList().size() > 0);
        System.out.println(paged);
    }

    @Test
    void getReviewedArticle() {
        PageInfo<Article> paged = editorService.getReviewedArticlesPaged(33, 1,  5);
        assertTrue(paged.getList().size() > 0);
        System.out.println(paged);
    }

    @Test
    void giveReviewToArticle() {
        Review review = new Review();
        review.setArticleId(31);
        review.setReviewerId(13);
        review.setContent("这是一篇文章的评审");
        review.setType(ReviewType.preliminaryReview);
        editorService.giveReviewToArticle(review, ReviewResult.pass);
    }
}
