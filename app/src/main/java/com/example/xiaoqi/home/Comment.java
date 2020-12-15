package com.example.xiaoqi.home;

public class Comment {
    private int commentId;
    private int noteId;
    private String authorPhone;
    private String authorAvatar;
    private String authorName;
    private String content;
    private String date;
    private String likes;

    public Comment() {
    }

    public Comment(int commentId, int noteId, String authorPhone, String authorAvatar, String authorName, String content, String date, String likes) {
        this.commentId = commentId;
        this.noteId = noteId;
        this.authorPhone = authorPhone;
        this.authorAvatar = authorAvatar;
        this.authorName = authorName;
        this.content = content;
        this.date = date;
        this.likes = likes;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getAuthorPhone() {
        return authorPhone;
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", noteId=" + noteId +
                ", authorPhone='" + authorPhone + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}
