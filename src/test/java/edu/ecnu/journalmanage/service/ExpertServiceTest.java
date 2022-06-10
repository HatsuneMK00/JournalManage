package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpertServiceTest {
    @Autowired
    ExpertService expertService;

    @Test
    void getToReviewArticlesPaged() {
        PageInfo<Article> toReviewArticlesPaged = expertService.getToReviewArticlesPaged(34, 1, 5);
        System.out.println(toReviewArticlesPaged);
    }

    @Test
    void getReviewedArticlesPaged() {
        PageInfo<Article> reviewedArticlesPaged = expertService.getReviewedArticlesPaged(34, 1, 5);
        System.out.println(reviewedArticlesPaged);
    }
}
