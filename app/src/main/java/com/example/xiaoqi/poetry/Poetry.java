package com.example.xiaoqi.poetry;

import java.io.Serializable;

public class Poetry implements Serializable {
    private int imageid;
    private String type;
    private String name;
    private String dynasty;
    private String author;
    private String content;

    public Poetry() {
    }

    public Poetry(int imageid, String type, String name, String dynasty, String author, String content){
        this.imageid=imageid;
        this.type=type;
        this.name=name;
        this.dynasty=dynasty;
        this.author=author;
        this.content=content;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
