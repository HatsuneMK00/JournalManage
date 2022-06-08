package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.mapper.ArticleMapper;
import edu.ecnu.journalmanage.mapper.ReviewMapper;
import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ExpertService {
    final ArticleMapper articleMapper;
    final ReviewMapper reviewMapper;
    final UserMapper userMapper;

    public ExpertService(ArticleMapper articleMapper, ReviewMapper reviewMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    public List<Article> getToReviewArticles(int expertId) {
        return articleMapper.getArticlesByExpert(expertId).stream().filter(
                article -> article.getStatus() == ArticleStatus.expertReview ||
                        article.getStatus() == ArticleStatus.expertRevision)
                .collect(java.util.stream.Collectors.toList());
    }

    public PageInfo<Article> getToReviewArticlesPaged(int expertId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(this.getToReviewArticles(expertId));
    }

    public List<Article> getReviewedArticles(int expertId) {
        List<Article> articles = articleMapper.getArticlesByExpert(expertId);
        Stream<Article> reviewed = articles.stream().filter(article -> article.getStatus() != ArticleStatus.editorReview &&
                article.getStatus() != ArticleStatus.editorRevision);
        return reviewed.collect(java.util.stream.Collectors.toList());
    }

    public PageInfo<Article> getReviewedArticlesPaged(int expertId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(this.getReviewedArticles(expertId));
    }

    public String giveReviewToArticle(Review review, ReviewResult result) {
        int affected = reviewMapper.addReviewToArticle(review);
        if (affected == 0) {
            return "fail to add review";
        }
        User chief = userMapper.getUsersByRole(Role.chiefEditor).get(0);
        switch (result) {
            case pass:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.chiefEditorReview);
                articleMapper.updateArticleChiefEditor(review.getArticleId(), chief.getId());
                break;
            case reject:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.expertRejection);
                break;
            case revise:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.expertReturned);
                break;
        }
        return null;
    }
}
