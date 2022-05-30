package edu.ecnu.journalmanage.mapper;

import edu.ecnu.journalmanage.model.Review;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ReviewMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into review(reviewer_id, article_id, content, type, update_time, create_time) " +
            "values(#{reviewerId}, #{articleId}, #{content}, #{type}, #{updateTime}, #{createTime})")
    int addReviewToArticle(Review review);


}
