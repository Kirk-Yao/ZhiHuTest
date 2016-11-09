package com.example.k.zhihudaily.response;

import com.example.k.zhihudaily.entity.EditorBean;
import com.example.k.zhihudaily.entity.ThemeStoryBean;

import java.util.List;

/**
 * Created by K on 2016/11/8.
 */

public class OtherThemeResponse {

    private List<ThemeStoryBean> stories;
    private String description;
    private String background;
    private long color;
    private String name;
    private String image;
    private List<EditorBean> editors;
    private String image_source;

    public List<ThemeStoryBean> getStories() {
        return stories;
    }

    public void setStories(List<ThemeStoryBean> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<EditorBean> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorBean> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
