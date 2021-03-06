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
public class EditorService {
    final ArticleMapper articleMapper;
    final ReviewMapper reviewMapper;
    final UserMapper userMapper;

    public EditorService(ArticleMapper articleMapper, ReviewMapper reviewMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    public List<Article> getAllUnbindArticles() {
        List<Article> articles = articleMapper.getAllArticles();
        Stream<Article> unbindArticles = articles.stream().filter(article -> article.getEditorId() == null);
        return unbindArticles.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取所有未绑定编辑的文章 即文章池
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getAllUnbindArticlesPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(articleMapper::getAllUnbindArticles);
    }

    public List<Article> getToReviewArticles(int editorId) {
        List<Article> articles = articleMapper.getAllArticles();
        Stream<Article> toReview = articles.stream().filter(article -> article.getEditorId() == editorId);
        return toReview.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取绑定到该编辑的文章 包括点击了审稿但未提交审稿意见的文章
     * @param editorId
     * @return
     */
    public PageInfo<Article> getToReviewArticlesPaged(int editorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> articleMapper.getToReviewArticlesByEditor(editorId));
    }

    public List<Article> getReviewedArticles(int editorId) {
        List<Article> articles = articleMapper.getArticlesByEditor(editorId);
        Stream<Article> reviewed = articles.stream().filter(article -> article.getStatus() != ArticleStatus.editorReview &&
                article.getStatus() != ArticleStatus.editorRevision);
        return reviewed.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取该编辑已经审稿的文章
     * @param editorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getReviewedArticlesPaged(int editorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> articleMapper.getReviewedArticlesByEditor(editorId));
    }

    public List<User> getAllExpert() {
        return userMapper.getUsersByRole(Role.expert);
    }

    /**
     * 获取可以选择的专家
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<User> getAllExpertPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(this::getAllExpert);
    }

    /**
     * 点击提交审稿意见时调用 将编辑分配的专家与文章绑定
     * @param articleId
     * @param expertId
     * @return
     */
    public String assignExpert(int articleId, int expertId) {
        return articleMapper.updateArticleExpert(articleId, expertId) == 1 ? null : "fail to assign expert";
    }

    /**
     * 绑定一个编辑与一篇文章
     * @param articleId
     * @param editorId
     * @return
     */
    public String bindEditor(int articleId, int editorId) {
        Integer editor = articleMapper.getArticleEditor(articleId);
        if (editor != null && editor != editorId) {
            return "editor already assigned";
        }
        articleMapper.updateArticleEditor(articleId, editorId);
        return null;
    }

    /**
     * 点击提高审稿意见时调用 将审稿意见与文章绑定
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
                affected = articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.expertReview);
                break;
            case reject:
                affected = articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.editorRejection);
                break;
            case revise:
                affected = articleMapper.updateArticleStatus(review.getArticleId(), ArticleStatus.editorReturned);
                break;
        }
        if (affected == 0) {
            return "fail to update article status";
        }
        return null;
    }

}
