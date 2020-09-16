package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
