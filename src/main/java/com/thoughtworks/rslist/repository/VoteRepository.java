package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePO,Integer> {
    @Override
    List<VotePO> findAll();

    VotePO findByUserPO(UserPO userPO);

    @Query("select v from Vote v where v.voteTime between :startTime and :endTime")
    List<VotePO> findAllByDate(LocalDateTime startTime, LocalDateTime endTime,Pageable pageable);


    @Query("select v from Vote v where v.rsEventId=:rsEventId and v.userPO.id=:userId")
    List<VotePO> findByRsEventIdAndUseIdINPage(Integer rsEventId, Integer userId, Pageable pageable);
}
