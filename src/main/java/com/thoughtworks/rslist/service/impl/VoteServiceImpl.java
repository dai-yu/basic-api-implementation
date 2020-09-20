package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteServiceImpl implements VoteService {
    VoteRepository voteRepository;
    RsEventRepository rsEventRepository;
    UserRepository  userRepository;

    public VoteServiceImpl(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Vote> findVoteByDate(LocalDateTime startTime, LocalDateTime endTime, Pageable pageIndex) {
        return voteRepository.findAllByDate(startTime,endTime,pageIndex)
                .stream()
                .map(votePO -> VotePoToVote(votePO))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vote> findByRsEventIdAndUseIdINPage(Integer rsEventId, Integer userId, Pageable pageIndex) {
        return voteRepository.findByRsEventIdAndUseIdINPage(rsEventId,userId,pageIndex)
                .stream()
                .map(votePO -> VotePoToVote(votePO))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void voteToRsEvent(RsEventPO rsEventPO, VotePO votePO, UserPO userPO) {
        rsEventRepository.save(rsEventPO);
        voteRepository.save(votePO);
        userRepository.save(userPO);
    }


    private Vote VotePoToVote(VotePO votePO){
        return Vote.builder()
                .userId(votePO.getUserPO().getId())
                .rsEventId(votePO.getRsEventId())
                .voteNum(votePO.getVoteNum())
                .voteTime(votePO.getVoteTime()).build();
    }



}
