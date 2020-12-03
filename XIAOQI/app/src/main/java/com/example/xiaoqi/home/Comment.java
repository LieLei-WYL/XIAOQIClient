package com.example.xiaoqi.home;

public class Comment {
    private String authorPhone;
    private String content;
    private String date;
    private int like;

    public Comment() {
    }

    public Comment(String authorPhone, String content, String date, int like) {
        this.authorPhone = authorPhone;
        this.content = content;
        this.date = date;
        this.like = like;
    }

    public String getAuthorPhone() {
        return authorPhone;
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "authorPhone='" + authorPhone + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", like=" + like +
                '}';
    }
}
