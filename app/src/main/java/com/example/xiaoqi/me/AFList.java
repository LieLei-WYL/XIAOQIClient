package com.example.xiaoqi.me;

public class AFList {
    private String phone;
    private String avatar;
    private String name;
    private String profile;
    private int followFlag;

    public AFList() {
    }

    public AFList(String phone, String avatar, String name, String profile, int followFlag) {
        this.phone = phone;
        this.avatar = avatar;
        this.name = name;
        this.profile = profile;
        this.followFlag = followFlag;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getFollowFlag() {
        return followFlag;
    }

    public void setFollowFlag(int followFlag) {
        this.followFlag = followFlag;
    }

    @Override
    public String toString() {
        return "AFList{" +
                "phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", followFlag=" + followFlag +
                '}';
    }
}
