package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleMapperTest {
    @Autowired
    ArticleMapper articleMapper;

    @Test
    void addArticle() {
        Article article = new Article();
        article.setAuthorId(13);
        article.setAbstractText("abstract");
        article.setTitle("title");
        article.setKeywords("keywords");
        article.setFilePath("filePath");
        int affected = articleMapper.addArticle(article);
        assertEquals(1, affected);
    }

    @Test
    void getArticleById() {
        Article article = articleMapper.getArticleById(2);
        assertNotNull(article);
        Date createTime = article.getCreateTime();
        // format create time in YYYY-MM-DD HH:MM:SS format such as 2022-05-30 18:12:21
        String createTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
        assertEquals("2022-05-30 18:12:21", createTimeStr);
    }

    @Test
    void getArticleByAuthorId() {
        List<Article> articles = articleMapper.getArticlesByAuthor(13);
        assertTrue(articles.size() > 0);
    }

    @Test
    void getArticleByStatus() {
        List<Article> articles = articleMapper.getArticlesByStatus(13, ArticleStatus.editorReview);
        assertTrue(articles.size() > 0);
    }

    @Test
    void getReviewOfArticle() {
        List<Review> reviews = articleMapper.getReviewByArticle(2);
        assertTrue(reviews.size() > 0);
    }

    @Test
    void getReviewOfArticleWithNoReview() {
        List<Review> reviews = articleMapper.getReviewByArticle(3);
        assertEquals(0, reviews.size());
    }

    @Test
    void getArticleEditor() {
        Integer editorId = articleMapper.getArticleEditor(2);
        assertNull(editorId);
    }

    @Test
    void getArticleWithNoEditor() {
        List<Article> articles = articleMapper.getAllArticles();
        System.out.println(articles);
        assertNull(articles.get(articles.size() - 1).getEditorId());
    }
}
