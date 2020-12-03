package com.example.xiaoqi.poetry;

import java.util.List;

public class Gushici {
    private int imageid;
    private  String type;
    private String name;
    private String dynasty;
    private String author;
    private String content;
    public Gushici(int imageid,String type,String name,String dynasty,String author,String content){
        this.imageid=imageid;
        this.type=type;
        this.name=name;
        this.dynasty=dynasty;
        this.author=author;
        this.content=content;
    }

    public int getImageid(){
       return imageid;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDynasty() {
        return dynasty;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
