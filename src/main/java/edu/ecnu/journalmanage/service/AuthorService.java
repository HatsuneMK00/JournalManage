package edu.ecnu.journalmanage.service;

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

    public String submitArticle(@NotNull Article article) {
        return articleMapper.addArticle(article) == 1 ? null : "Add article failed";
    }

    public List<Article> getAcceptedArticles(int authorId) {
        List<Article> articles = articleMapper.getArticlesByAuthor(authorId);
        Stream<Article> accepted = articles.stream().filter((element) -> element.getStatus() == ArticleStatus.accepted);
        return accepted.collect(java.util.stream.Collectors.toList());
    }

    public List<Article> getInProgressArticles(int authorId) {
        List<Article> articles = articleMapper.getArticlesByAuthor(authorId);
        Stream<Article> accepted = articles.stream().filter((element) -> {
            ArticleStatus status = element.getStatus();
            return status == ArticleStatus.submitted ||
                    status == ArticleStatus.editorRevision ||
                    status == ArticleStatus.expertRevision ||
                    status == ArticleStatus.chiefEditorRevision;
        });
        return accepted.collect(java.util.stream.Collectors.toList());
    }

    public Map<String, List<Review>> getReviewOfArticle(int articleId) {
        List<Review> reviews = articleMapper.getReviewByArticle(articleId);
        Map<String, List<Review>> reviewMap = new HashMap<>();
        reviewMap.put(ReviewType.preliminaryReview.getString(), new ArrayList<>());
        reviewMap.put(ReviewType.preliminaryRebuttalReview.getString(), new ArrayList<>());
        reviewMap.put(ReviewType.externalReview.getString(), new ArrayList<>());
        reviewMap.put(ReviewType.externalRebuttalReview.getString(), new ArrayList<>());
        reviewMap.put(ReviewType.finalReview.getString(), new ArrayList<>());
        reviewMap.put(ReviewType.finalRebuttalReview.getString(), new ArrayList<>());
        reviews.stream().map((element) -> reviewMap.get(element.getType().getString()).add(element));
        return reviewMap;
    }

}
