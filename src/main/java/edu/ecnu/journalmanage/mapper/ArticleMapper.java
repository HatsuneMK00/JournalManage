package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Article;
import edu.ecnu.journalmanage.model.ArticleStatus;
import edu.ecnu.journalmanage.model.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into article(title, author_id, authors, abstract_text, keywords, file_path, status, " +
            "create_time, update_time) values(#{title}, #{authorId}, #{authors}, " +
            "#{abstractText}, #{keywords}, #{filePath}, #{status}, #{createTime}, #{updateTime})")
    int addArticle(Article article);

    @Update("update article set title=#{title}, authors=#{authors}, abstract_text=#{abstractText}, " +
            "keywords=#{keywords}, file_path=#{filePath}, update_time=#{updateTime} " +
            "where id=#{id}")
    int updateArticle(Article article);

    @Update("update article set status=#{status} where id=#{articleId}")
    int updateArticleStatus(int articleId, ArticleStatus status);

    @Update("update article set expert_id=#{expertId} where id=#{articleId}")
    int updateArticleExpert(int articleId, int expertId);

    @Update("update article set editor_id=#{editorId} where id=#{articleId}")
    int updateArticleEditor(int articleId, int editorId);

    @Select("select editor_id from article where id=#{articleId}")
    Integer getArticleEditor(int articleId);

    @Update("update article set chief_editor_id=#{chiefEditorId} where id=#{articleId}")
    int updateArticleChiefEditor(int articleId, int chiefEditorId);


    @Select("select * from article where status=#{status} and author_id=#{authorId} order by update_time desc")
    List<Article> getArticlesByStatus(int authorId, ArticleStatus status);

    @Select("select * from article where author_id=#{authorId} order by update_time desc")
    List<Article> getArticlesByAuthor(int authorId);

    @Select("select * from article where editor_id=#{editorId} order by update_time desc")
    List<Article> getArticlesByEditor(int editorId);

    @Select("select * from article where expert_id=#{expertId} order by update_time desc")
    List<Article> getArticlesByExpert(int expertId);

    @Select("select * from article where chief_editor_id=#{chiefEditorId} order by update_time desc")
    List<Article> getArticlesByChiefEditor(int chiefEditorId);

    @Select("select * from article order by update_time desc")
    List<Article> getAllArticles();

    @Select("select * from article where id=#{id}")
    Article getArticleById(int id);

    @Select("select * from review where article_id=#{articleId} order by update_time desc")
    List<Review> getReviewByArticle(int articleId);

    @Select("select * from article where status=${@edu.ecnu.journalmanage.model.ArticleStatus@accepted.ordinal()} and author_id=#{authorId} order by update_time desc")
    List<Article> getAcceptedArticlesByAuthor(int authorId);

    @Select("select * from article where author_id=#{authorId} and " +
            "(status!=${@edu.ecnu.journalmanage.model.ArticleStatus@editorRejection.ordinal()} and " +
            "status!=${@edu.ecnu.journalmanage.model.ArticleStatus@expertRejection.ordinal()} and " +
            "status!=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorRejection.ordinal()} and " +
            "status!=${@edu.ecnu.journalmanage.model.ArticleStatus@accepted.ordinal()} or " +
            "status is NULL) order by update_time desc")
    List<Article> getInProgressArticlesByAuthor(int authorId);

    @Select("select * from article where author_id=#{authorId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorRejection.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@expertRejection.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorRejection.ordinal()}) order by update_time desc")
    List<Article> getRejectedArticlesByAuthor(int authorId);

    @Select("select * from article where chief_editor_id=#{chiefEditorId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorReview.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorRevision.ordinal()}) order by update_time desc")
    List<Article> getToReviewArticlesByChiefEditor(int chiefEditorId);

    @Select("select * from article where chief_editor_id=#{chiefEditorId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorRejection.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@chiefEditorReturned.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@accepted.ordinal()}) order by update_time desc")
    List<Article> getReviewedArticlesByChiefEditor(int chiefEditorId);

    @Select("select * from article where " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorReview.ordinal()} or " +
            "status is NULL order by update_time desc")
    List<Article> getAllUnbindArticles();

    @Select("select * from article where editor_id=#{editorId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorReview.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorRevision.ordinal()}) order by update_time desc")
    List<Article> getToReviewArticlesByEditor(int editorId);

    @Select("select * from article where editor_id=#{editorId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorRejection.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@editorReturned.ordinal()} or " +
            "status>${@edu.ecnu.journalmanage.model.ArticleStatus@editorRevision.ordinal()}) order by update_time desc")
    List<Article> getReviewedArticlesByEditor(int editorId);

    @Select("select * from article where expert_id=#{expertId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@expertReview.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@expertRevision.ordinal()}) order by update_time desc")
    List<Article> getToReviewArticlesByExpert(int expertId);

    @Select("select * from article where expert_id=#{expertId} and " +
            "(status=${@edu.ecnu.journalmanage.model.ArticleStatus@expertRejection.ordinal()} or " +
            "status=${@edu.ecnu.journalmanage.model.ArticleStatus@expertReturned.ordinal()} or " +
            "status>${@edu.ecnu.journalmanage.model.ArticleStatus@expertRevision.ordinal()}) order by update_time desc")
    List<Article> getReviewedArticlesByExpert(int expertId);
}
