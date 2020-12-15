package com.example.xiaoqi.home;

import android.app.Application;

import com.example.xiaoqi.me.User;
import com.example.xiaoqi.poetry.Poetry;

import java.util.ArrayList;

public class Global extends Application {
    private int userState;
    private String currentUserPhone;
    private String path = "http://10.7.89.113:8080";
    private ArrayList<Note> findNoteList;
    private ArrayList<Note> attentionNoteList;
    private ArrayList<Note> nearbyNoteList;
    private ArrayList<Note> MyNoteList;
    private ArrayList<Note> MyLikeList;
    private ArrayList<Note> MyCollectList;

    private ArrayList<Poetry> poetryList;
    private int curItem = 0;

    private User user;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        findNoteList = new ArrayList<>();
        attentionNoteList = new ArrayList<>();
        nearbyNoteList = new ArrayList<>();

        MyNoteList = new ArrayList<>();
        MyLikeList = new ArrayList<>();
        MyCollectList = new ArrayList<>();

        poetryList = new ArrayList<>();

        user = new User();
        super.onCreate();
    }

    public String getCurrentUserPhone() {
        return currentUserPhone;
    }

    public void setCurrentUserPhone(String currentUserPhone) {
        this.currentUserPhone = currentUserPhone;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Note> getFindNoteList() {
        return findNoteList;
    }

    public void setFindNoteList(ArrayList<Note> findNoteList) {
        this.findNoteList = findNoteList;
    }

    public ArrayList<Note> getAttentionNoteList() {
        return attentionNoteList;
    }

    public void setAttentionNoteList(ArrayList<Note> attentionNoteList) {
        this.attentionNoteList = attentionNoteList;
    }

    public ArrayList<Note> getNearbyNoteList() {
        return nearbyNoteList;
    }

    public void setNearbyNoteList(ArrayList<Note> nearbyNoteList) {
        this.nearbyNoteList = nearbyNoteList;
    }

    public ArrayList<Note> getMyNoteList() {
        return MyNoteList;
    }

    public void setMyNoteList(ArrayList<Note> myNoteList) {
        MyNoteList = myNoteList;
    }

    public ArrayList<Note> getMyLikeList() {
        return MyLikeList;
    }

    public void setMyLikeList(ArrayList<Note> myLikeList) {
        MyLikeList = myLikeList;
    }

    public ArrayList<Note> getMyCollectList() {
        return MyCollectList;
    }

    public void setMyCollectList(ArrayList<Note> myCollectList) {
        MyCollectList = myCollectList;
    }

    public ArrayList<Poetry> getPoetryList() {
        return poetryList;
    }

    public void setPoetryList(ArrayList<Poetry> poetryList) {
        this.poetryList = poetryList;
    }

    public int getCurItem() {
        return curItem;
    }

    public void setCurItem(int curItem) {
        this.curItem = curItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
