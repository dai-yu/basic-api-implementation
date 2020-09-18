package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPO,Integer> {
    @Override
    List<RsEventPO> findAll();

    @Query(value = "select * from rs_event limit :start,:end",nativeQuery = true)
    List<RsEventPO> findLimit(int start,int end);

    @Query(value = "select * from rs_event limit :index,1",nativeQuery = true)
    RsEventPO findOne(int index);
}
