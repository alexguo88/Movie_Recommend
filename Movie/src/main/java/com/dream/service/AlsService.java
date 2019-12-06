package com.dream.service;

import com.dream.po.Movie;

import java.util.List;

/**
 * Created by ZXL on 2018/3/29.
 */


public interface AlsService {
    /**
     * 选择推荐电影
     *
     * @param userid 用户编号
     * @return
     */
    List<Movie> selectAlsMoviesByUserId(Integer userid);
}
