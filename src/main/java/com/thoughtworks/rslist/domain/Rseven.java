package com.thoughtworks.rslist.domain;

public class Rseven {
    private String name;
    private String type;

    public Rseven(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public Rseven(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
