package edu.ecnu.journalmanage.model;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private int id;
    private String title;
    private String abstractText;
    private int authorId;
    private int editorId;
    private int expertId;
    private int chiefEditorId;
    private ArticleStatus status = ArticleStatus.editorReview;
    private String filePath;
    private Date createTime = new Date();
    private Date updateTime = new Date();
    private String keywords;
}
