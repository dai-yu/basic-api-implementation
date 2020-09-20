package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {

    List<Vote> findVoteByDate(LocalDateTime startTime, LocalDateTime endTime, Pageable pageIndex);

    List<Vote> findByRsEventIdAndUseIdINPage(Integer rsEventId, Integer userId, Pageable pageIndex);

    void voteToRsEvent(RsEventPO rsEventPO, VotePO votePO, UserPO userPO);
}
