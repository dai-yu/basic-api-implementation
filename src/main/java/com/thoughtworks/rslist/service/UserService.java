package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.po.UserPO;

import java.util.Optional;

public interface UserService {
    Optional<UserPO> findById(int userId);
}
