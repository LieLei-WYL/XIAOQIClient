package com.example.xiaoqi.news;

public class DialogueMain {
    private String phone;
    private String avatar;
    private String currentAvatar;
    private String name;
    private String lastTime;
    private String lastSentence;

    public DialogueMain() {
    }

    public DialogueMain(String phone, String avatar, String currentAvatar, String name, String lastTime, String lastSentence) {
        this.phone = phone;
        this.avatar = avatar;
        this.currentAvatar = currentAvatar;
        this.name = name;
        this.lastTime = lastTime;
        this.lastSentence = lastSentence;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(String currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastSentence() {
        return lastSentence;
    }

    public void setLastSentence(String lastSentence) {
        this.lastSentence = lastSentence;
    }

    @Override
    public String toString() {
        return "DialogueMain{" +
                "phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", currentAvatar='" + currentAvatar + '\'' +
                ", name='" + name + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", lastSentence='" + lastSentence + '\'' +
                '}';
    }
}
