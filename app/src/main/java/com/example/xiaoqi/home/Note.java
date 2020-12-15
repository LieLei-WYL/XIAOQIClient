package com.example.xiaoqi.home;

import java.io.Serializable;
import java.util.Arrays;

public class Note implements Serializable {
    private int noteId;
    private String authorPhone;
    private String authorAvatar;
    private String authorName;
    private String[] images;
    private String title;
    private String content;
    private String topic;
    private String date;
    private String area;

    public Note(){}

    public Note(int noteId, String authorPhone, String authorAvatar, String authorName, String[] images, String title, String content, String topic, String date, String area) {
        this.noteId = noteId;
        this.authorPhone = authorPhone;
        this.authorAvatar = authorAvatar;
        this.authorName = authorName;
        this.images = images;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.date = date;
        this.area = area;
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

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", authorPhone='" + authorPhone + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", images=" + Arrays.toString(images) +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", topic='" + topic + '\'' +
                ", date='" + date + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
