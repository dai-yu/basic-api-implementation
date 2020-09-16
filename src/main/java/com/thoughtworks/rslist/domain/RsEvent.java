package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String name;
    private String keyword;
    private User user;

    public RsEvent(String name, String keyword,User user) {
        this.name = name;
        this.keyword = keyword;
        this.user=user;
    }
    public RsEvent(String name, String keyword) {
        this.name = name;
        this.keyword = keyword;
    }
    public RsEvent(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
