package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.ArticleMapper;
import edu.ecnu.journalmanage.model.Article;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    final ArticleMapper articleMapper;

    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public Article getArticleById(int articleId) {
        return articleMapper.getArticleById(articleId);
    }
}
