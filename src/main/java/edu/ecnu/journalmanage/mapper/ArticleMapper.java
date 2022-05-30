package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into article(title, author_id, abstract_text, keywords, file_path, status, " +
            "create_time, update_time) values(#{title}, #{authorId}, " +
            "#{abstractText}, #{keywords}, #{filePath}, #{status}, #{createTime}, #{updateTime})")
    int addArticle(Article article);

    @Update("update article set title=#{title}, author=#{author}, abstract_text=#{abstractText}, " +
            "keywords=#{keywords}, file_path=#{filePath}, status=#{status}, update_time=#{updateTime} " +
            "where id=#{id}")
    int updateArticle(Article article);

    @Select("select * from article where status=#{status} and author_id=#{authorId}")
    List<Article> getArticlesByStatus(int authorId, ArticleStatus status);

    @Select("select * from article where author_id=#{authorId}")
    List<Article> getArticlesByAuthor(int authorId);

    @Select("select * from article where id=#{id}")
    Article getArticleById(int id);

    @Select("select * from review where article_id=#{articleId} order by update_time desc")
    List<Review> getReviewByArticle(int articleId);

}
