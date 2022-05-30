package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.ArticleMapper;
import edu.ecnu.journalmanage.mapper.ReviewMapper;
import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class EditorService {
    final ArticleMapper articleMapper;
    final ReviewMapper reviewMapper;
    final UserMapper userMapper;

    public EditorService(ArticleMapper articleMapper, ReviewMapper reviewMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    public List<Article> getToReviewArticles(int editorId) {
        List<Article> articles = articleMapper.getAllArticles();
        Stream<Article> toReview = articles.stream().filter(article -> article.getStatus() == ArticleStatus.editorReview ||
                article.getStatus() == ArticleStatus.editorRevision && article.getEditorId() == editorId);
        return toReview.collect(java.util.stream.Collectors.toList());
    }

    public List<Article> getReviewedArticles(int editorId) {
        List<Article> articles = articleMapper.getArticlesByEditor(editorId);
        Stream<Article> reviewed = articles.stream().filter(article -> article.getStatus() != ArticleStatus.editorReview &&
                article.getStatus() != ArticleStatus.editorRevision);
        return reviewed.collect(java.util.stream.Collectors.toList());
    }

    public List<User> getAllExpert() {
        return userMapper.getUsersByRole(Role.expert);
    }

    public String assignExpert(int articleId, int expertId) {
        return articleMapper.updateArticleExpert(articleId, expertId) == 1 ? null : "fail to assign expert";
    }

    public String giveReviewToArticle(Review review, ReviewResult result) {
        int affected = reviewMapper.addReviewToArticle(review);
        if (affected == 0) {
            return "fail to add review";
        }
        switch (result) {
            case pass:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.expertRevision);
                break;
            case reject:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.editorRejection);
                break;
            case revise:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.editorReturned);
                break;
        }
        articleMapper.updateArticleEditor(review.getArticleId(), review.getReviewerId());
        return null;
    }


}
