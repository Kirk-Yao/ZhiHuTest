package com.example.k.zhihudaily.response;

import com.example.k.zhihudaily.entity.CommentBean;

import java.util.List;

/**
 * Created by K on 2016/11/9.
 */

public class CommentResponse {

    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
