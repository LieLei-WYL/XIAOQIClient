package com.example.xiaoqi.news;

public class Dialogue {
    private String sentence;
    private String time;
    private int type;//0表示发送，1表示接收

    public Dialogue() {
    }

    public Dialogue(String sentence, String time, int type) {
        this.sentence = sentence;
        this.time = time;
        this.type = type;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Dialogue{" +
                "sentence='" + sentence + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}
