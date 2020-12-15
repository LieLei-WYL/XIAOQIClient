package com.example.xiaoqi.home;

import java.util.ArrayList;

public class CommentInfo {
    private String[] images;
    private String likes;
    private String collections;
    private String comments;
    private ArrayList<Comment> commentLidt;

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<Comment> getCommentLidt() {
        return commentLidt;
    }

    public void setCommentLidt(ArrayList<Comment> commentLidt) {
        this.commentLidt = commentLidt;
    }
}
