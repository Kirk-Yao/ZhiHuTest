package com.example.k.zhihudaily.response;

import com.example.k.zhihudaily.entity.StoryBean;

import java.util.List;

/**
 * Created by K on 2016/11/8.
 */

public class BeforeNewsResponse {
    private String date;
    private List<StoryBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoryBean> getStories() {
        return stories;
    }

    public void setStories(List<StoryBean> stories) {
        this.stories = stories;
    }
}
