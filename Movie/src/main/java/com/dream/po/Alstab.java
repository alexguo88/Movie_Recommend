package com.dream.po;

/**
 *
 *
 */
public class Alstab {
    private Integer userid; //用户编号
    private Integer movieid; //电影编号
    private Double rating; //打分

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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}