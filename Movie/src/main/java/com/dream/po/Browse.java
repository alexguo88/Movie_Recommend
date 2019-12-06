package com.dream.po;

import java.util.Date;

/**
 * 用户浏览记录
 *
 */
public class Browse {
    private Integer browseid; //主键

    private Integer userid; //用户编号
    private String movieids; //电影编号
    private Date browsetime; // 浏览时间

    public Integer getBrowseid() {
        return browseid;
    }

    public void setBrowseid(Integer browseid) {
        this.browseid = browseid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getmovieids() {
        return movieids;
    }

    public void setmovieids(String movieids) {
        this.movieids = movieids;
    }

    public Date getBrowsetime() {
        return browsetime;
    }

    public void setBrowsetime(Date browsetime) {
        this.browsetime = browsetime;
    }
}
