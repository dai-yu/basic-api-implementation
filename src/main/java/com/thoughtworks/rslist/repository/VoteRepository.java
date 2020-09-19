package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VotePO,Integer> {
    @Override
    List<VotePO> findAll();

    VotePO findByUserPO(UserPO userPO);

    @Query(value = "select * from Vote where vote_time between :startTime and :endTime",nativeQuery = true)
    List<VotePO> findAllByDate(String startTime, String endTime);
}
