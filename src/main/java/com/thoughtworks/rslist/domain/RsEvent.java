package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String name;
    private String keyword;

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
}
