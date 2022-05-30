package edu.ecnu.journalmanage.model;

import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private Integer id;
    private Integer reviewerId;
    private Integer articleId;
    private String comment;
    private Date createTime = new Date();
    private Date updateTime = new Date();
}
