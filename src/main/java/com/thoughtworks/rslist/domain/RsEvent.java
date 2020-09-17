package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String eventName;
    private String keyword;
    private int userId;

    public RsEvent(String eventName, String keyword, int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }
    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
    public RsEvent(){

    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    @JsonIgnore
    public int getUserId() {
        return userId;
    }

//    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
