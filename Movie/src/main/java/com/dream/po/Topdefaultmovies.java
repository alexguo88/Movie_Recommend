package com.dream.po;

/**
 * 默认电影
 *
 */
public class Topdefaultmovies {
    private Integer id;
    private Integer movieid;
    private String moviename;

    public Topdefaultmovies() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieid() {
        return this.movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }

    public String getMoviename() {
        return this.moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename == null ? null : moviename.trim();
    }
}
