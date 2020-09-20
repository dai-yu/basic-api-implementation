package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RsEventServiceImpl implements RsEventService {
    VoteRepository voteRepository;
    RsEventRepository rsEventRepository;
    UserRepository userRepository;

    public RsEventServiceImpl(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Optional<RsEventPO> findById(int rsEventId) {
        return rsEventRepository.findById(rsEventId);
    }

}
