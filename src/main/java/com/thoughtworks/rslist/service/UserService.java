package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.po.UserPO;

import java.util.List;

public interface UserService {
    UserPO findById(int userId);

    void save(UserPO userPO);

    List<UserPO> findAll();

    void deleteById(int id);
}
