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

    public PageInfo<Article> getAllUnbindArticlesPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> this.getAllUnbindArticles());
    }

    public List<Article> getToReviewArticles(int editorId) {
        List<Article> articles = articleMapper.getAllArticles();
        Stream<Article> toReview = articles.stream().filter(article -> article.getEditorId() == editorId);
        return toReview.collect(java.util.stream.Collectors.toList());
    }

    public PageInfo<Article> getToReviewArticlesPaged(int editorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.getToReviewArticles(editorId));
    }

    public List<Article> getReviewedArticles(int editorId) {
        List<Article> articles = articleMapper.getArticlesByEditor(editorId);
        Stream<Article> reviewed = articles.stream().filter(article -> article.getStatus() != ArticleStatus.editorReview &&
                article.getStatus() != ArticleStatus.editorRevision);
        return reviewed.collect(java.util.stream.Collectors.toList());
    }

    public PageInfo<Article> getReviewedArticlesPaged(int editorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.getReviewedArticles(editorId));
    }

    public List<User> getAllExpert() {
        return userMapper.getUsersByRole(Role.expert);
    }

    public PageInfo<User> getAllExpertPaged(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.getAllExpert());
    }

    public String assignExpert(int articleId, int expertId) {
        return articleMapper.updateArticleExpert(articleId, expertId) == 1 ? null : "fail to assign expert";
    }

    // 绑定文章的编辑
    public String bindEditor(int articleId, int editorId) {
        Integer editor = articleMapper.getArticleEditor(articleId);
        if (editor != null && editor != editorId) {
            return "editor already assigned";
        }
        articleMapper.updateArticleEditor(articleId, editorId);
        return null;
    }

    // 这个函数也有更新改文章的编辑为这条review的编辑的效果
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
        return this.bindEditor(review.getArticleId(), review.getReviewerId());
    }

}
