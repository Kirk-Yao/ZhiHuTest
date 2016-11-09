package com.example.k.zhihudaily.constants;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by K on 2016/11/7.
 */

public class MethodURL {

    //启动页
    public static final String GET_START_IMAGE = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    //首页最新消息
    public static final String HOME_LAST_NEWS = "http://news-at.zhihu.com/api/4/news/latest";

    //过去消息
    public static final String HOME_BEFORE_NEWS = "http://news.at.zhihu.com/api/4/news/before/";

    //文章详情
    public static final String NEWS_DETAIL = "http://news-at.zhihu.com/api/4/news/";

    //文章点赞数等信息
    public static final String NEWS_EXTRA_INFO = "http://news-at.zhihu.com/api/4/story-extra/";

    //分类文章
    public static final String NEWS_THEME = "http://news-at.zhihu.com/api/4/theme/";

    //获取某个专栏之前的新闻
    public static final String MORE_THEME_NEWS = "http://news-at.zhihu.com/api/4/theme/";

    //查看评论
    public static final String COMMENT = "http://news-at.zhihu.com/api/4/story/";
}
