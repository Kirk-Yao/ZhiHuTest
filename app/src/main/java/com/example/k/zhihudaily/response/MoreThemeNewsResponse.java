package com.example.k.zhihudaily.response;

import com.example.k.zhihudaily.entity.ThemeStoryBean;

import java.util.List;

/**
 * Created by K on 2016/11/9.
 */

public class MoreThemeNewsResponse {

    private List<ThemeStoryBean> stories;

    public List<ThemeStoryBean> getStories() {
        return stories;
    }

    public void setStories(List<ThemeStoryBean> stories) {
        this.stories = stories;
    }
}
