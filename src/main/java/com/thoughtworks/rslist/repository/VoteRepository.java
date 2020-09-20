package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VotePO,Integer> {
    @Override
    List<VotePO> findAll();

    VotePO findByUserPO(UserPO userPO);

    @Query("select v from Vote v where v.voteTime between :startTime and :endTime")
    List<VotePO> findAllByDate(LocalDateTime startTime, LocalDateTime endTime);
}
