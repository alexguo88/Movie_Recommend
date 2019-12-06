package com.dream.po;

import java.util.Date;

/**
 * 电影评星
 *
 */
public class Review {
    private Integer reviewid;

    private Integer userid; //用户编号
    private Integer movieid; //电影编号
    private String content; //内荣
    private Double star; //星数
    private Date reviewtime; //评星时间
    private String picture; //图片

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getReviewid() {
        return reviewid;
    }

    public void setReviewid(Integer reviewid) {
        this.reviewid = reviewid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getMovieid() {
        return movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public Date getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(Date reviewtime) {
        this.reviewtime = reviewtime;
    }
}
