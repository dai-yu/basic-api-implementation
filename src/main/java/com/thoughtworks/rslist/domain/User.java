package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max=8)
    @JsonProperty("user_name")
    private String name;
    @NotNull
    @JsonProperty("user_gender")
    private String gender;
    @NotNull
    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    private int age;
    @Email
    @JsonProperty("user_email")
    private String email;
    @Pattern(regexp = "1\\d{10}")
    @JsonProperty("user_phone")
    private String phone;
    private int voteNum=10;

    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
