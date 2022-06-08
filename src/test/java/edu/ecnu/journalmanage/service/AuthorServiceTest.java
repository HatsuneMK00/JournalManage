package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import edu.ecnu.journalmanage.model.ReviewType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorServiceTest {
    @Autowired
    AuthorService authorService;
    @Autowired
    FileService fileService;

    /**
     * 测试上传稿件
     */
    @Test
    void submitArticle() {
        Article article = new Article();
        article.setTitle("test");
        article.setAuthorId(13);
        article.setAbstractText("test");
        article.setKeywords("test");

//        Use fileService to upload file and get path
//        String path = fileService.generateUUidFilename(null);
//        fileService.saveFile(null, path);

//        use the path used in fileService
        article.setFilePath("test");

        authorService.submitArticle(article);

        assertEquals(article.getStatus(), ArticleStatus.editorReview);
        assertTrue(article.getId() > 0);
        assertNotNull(article.getCreateTime());
        assertNotNull(article.getUpdateTime());
    }

    /**
     * 测试稿件状态查询
     */
    @Test
    void getArticleStatus() {
        List<Article> acceptedArticles = authorService.getAcceptedArticles(13);
        assertTrue(acceptedArticles.size() > 0);
        acceptedArticles = authorService.getAcceptedArticles(14);
        assertEquals(0, acceptedArticles.size());

        List<Article> inProgressArticles = authorService.getInProgressArticles(13);
        assertTrue(inProgressArticles.size() > 0);
    }

    /**
     * 测试稿件查看详情
     */
    @Test
    void getArticleDetail() {
        Article article = authorService.getInProgressArticles(13).get(0);
        Map<ReviewType, List<Review>> review = authorService.getReviewOfArticle(article.getId());

        assertNotNull(article);
        assertEquals(1, article.getId());
    }

}
