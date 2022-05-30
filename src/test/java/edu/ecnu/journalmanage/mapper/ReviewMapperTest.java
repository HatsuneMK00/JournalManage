package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Review;
import edu.ecnu.journalmanage.model.ReviewType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewMapperTest {
    @Autowired
    ReviewMapper reviewMapper;

    @Test
    void addReview() {
        Review review = new Review();
        review.setReviewerId(14);
        review.setArticleId(2);
        review.setContent("这是一篇文章的评审");
        review.setType(ReviewType.preliminaryReview);
        int affected = reviewMapper.addReviewToArticle(review);
        assertEquals(1, affected);
    }

    @Test
    void addReviewWithoutType() {
        Review review = new Review();
        review.setReviewerId(14);
        review.setArticleId(2);
        review.setContent("这是一篇文章的评审");
//        review.setType(ReviewType.preliminaryReview);
        assertThrows(Exception.class, () -> reviewMapper.addReviewToArticle(review));
    }

}
