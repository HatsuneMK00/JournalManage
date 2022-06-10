package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.mapper.ArticleMapper;
import edu.ecnu.journalmanage.mapper.ReviewMapper;
import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import edu.ecnu.journalmanage.model.ReviewResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ChiefEditorService {
    final ArticleMapper articleMapper;
    final ReviewMapper reviewMapper;
    final UserMapper userMapper;

    public ChiefEditorService(ArticleMapper articleMapper, ReviewMapper reviewMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    public List<Article> getToReviewArticles(int chiefEditorId) {
        return articleMapper.getArticlesByChiefEditor(chiefEditorId).stream().filter(
                        article -> article.getStatus() == ArticleStatus.chiefEditorReview ||
                                article.getStatus() == ArticleStatus.chiefEditorRevision)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取绑定到该主编的文章
     * @param chiefEditorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getToReviewArticlesPaged(int chiefEditorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> articleMapper.getToReviewArticlesByChiefEditor(chiefEditorId));
    }

    public List<Article> getReviewedArticles(int chiefEditorId) {
        List<Article> articles = articleMapper.getArticlesByChiefEditor(chiefEditorId);
        Stream<Article> reviewed = articles.stream().filter(article -> article.getStatus() != ArticleStatus.chiefEditorReview &&
                article.getStatus() != ArticleStatus.chiefEditorRevision);
        return reviewed.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取该主编的所有已审文章
     * @param chiefEditorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getReviewedArticlesPaged(int chiefEditorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> articleMapper.getReviewedArticlesByChiefEditor(chiefEditorId));
    }

    /**
     * 提交主编的审核意见时调用
     * @param review
     * @param result
     * @return
     */
    public String giveReviewToArticle(Review review, ReviewResult result) {
        int affected = reviewMapper.addReviewToArticle(review);
        if (affected == 0) {
            return "fail to add review";
        }
        switch (result) {
            case pass:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.accepted);
                break;
            case reject:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.chiefEditorRejection);
                break;
            case revise:
                articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.chiefEditorReturned);
                break;
        }
        return null;
    }
}
