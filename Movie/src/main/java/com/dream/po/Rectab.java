package com.dream.po;

/**
 * 用户喜欢的5部电影
 *
 */
public class Rectab {

    private Integer userid;
    private String movieids;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMovieids() {
        return movieids;
    }

    public void setMovieids(String movieids) {
        this.movieids = movieids == null ? null : movieids.trim();
    }
}