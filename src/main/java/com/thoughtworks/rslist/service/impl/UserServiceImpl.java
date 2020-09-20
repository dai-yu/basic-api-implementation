package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    VoteRepository voteRepository;
    RsEventRepository rsEventRepository;
    UserRepository userRepository;

    public UserServiceImpl(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Optional<UserPO> findById(int userId) {
        return userRepository.findById(userId);
    }
}
