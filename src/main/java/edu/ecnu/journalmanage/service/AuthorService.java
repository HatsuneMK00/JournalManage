package edu.ecnu.journalmanage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.ecnu.journalmanage.mapper.ArticleMapper;
import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import edu.ecnu.journalmanage.model.ReviewType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class AuthorService {
    final
    ArticleMapper articleMapper;

    public AuthorService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 点击提交文章时调用 注意在调用前需要使用FileService获取到文件保存的路径 填充进Article对象中
     *
     * @param article
     * @return
     */
    public String submitArticle(@NotNull Article article) {
        int affected = articleMapper.addArticle(article);
        if (affected == 0) {
            return "Add article failed";
        }
        articleMapper.updateArticleStatus(article.getId(), ArticleStatus.editorReview);
        return null;
    }

    /**
     * 退修的文章提交时调用这个函数 因为涉及到文章状态 注意不要调用错了
     *
     * @param article
     * @return
     */
    public String submitRevisionArticle(@NotNull Article article) {
        int affected = articleMapper.updateArticle(article);
        if (affected == 0) {
            return "Update article failed";
        }
        switch (article.getStatus()) {
            case editorReturned:
                articleMapper.updateArticleStatus(article.getId(), ArticleStatus.editorRevision);
                break;
            case expertReturned:
                articleMapper.updateArticleStatus(article.getId(), ArticleStatus.expertRevision);
                break;
            case chiefEditorReturned:
                articleMapper.updateArticleStatus(article.getId(), ArticleStatus.chiefEditorRevision);
                break;
            default:
                return "Article status is not correct, failed to update article status";
        }
        return null;
    }

    public List<Article> getAcceptedArticles(int authorId) {
        List<Article> articles = articleMapper.getArticlesByAuthor(authorId);
        Stream<Article> accepted = articles.stream().filter((element) -> element.getStatus() == ArticleStatus.accepted);
        return accepted.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取当前用户的接受的文章
     * @param authorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getAcceptedArticlesPaged(int authorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> articleMapper.getAcceptedArticlesByAuthor(authorId));
    }

    public List<Article> getInProgressArticles(int authorId) {
        List<Article> articles = articleMapper.getArticlesByAuthor(authorId);
        Stream<Article> accepted = articles.stream().filter((element) -> {
            ArticleStatus status = element.getStatus();
            return status == ArticleStatus.editorReview ||
                    status == ArticleStatus.editorRevision ||
                    status == ArticleStatus.expertRevision ||
                    status == ArticleStatus.chiefEditorRevision;
        });
        return accepted.collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取当前用户的进行中的文章
     * @param authorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Article> getInProgressArticlesPaged(int authorId, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> articleMapper.getInProgressArticlesByAuthor(authorId));
    }

    /**
     * 在展示文章详情时调用 获取文章的所有review
     * review是一个map key为ReviewType value为Review对象的list 分别对应初审review 初审重审review等等6种
     * @param articleId
     * @return
     */
    public Map<ReviewType, List<Review>> getReviewOfArticle(int articleId) {
        List<Review> reviews = articleMapper.getReviewByArticle(articleId);
        Map<ReviewType, List<Review>> reviewMap = new HashMap<>();
        reviewMap.put(ReviewType.preliminaryReview, new ArrayList<>());
        reviewMap.put(ReviewType.preliminaryRebuttalReview, new ArrayList<>());
        reviewMap.put(ReviewType.externalReview, new ArrayList<>());
        reviewMap.put(ReviewType.externalRebuttalReview, new ArrayList<>());
        reviewMap.put(ReviewType.finalReview, new ArrayList<>());
        reviewMap.put(ReviewType.finalRebuttalReview, new ArrayList<>());
        reviews.stream().map((element) -> reviewMap.get(element.getType()).add(element));
        return reviewMap;
    }

}
