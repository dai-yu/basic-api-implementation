package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;

import java.util.List;
import java.util.Optional;

public interface RsEventService {
    Optional<RsEventPO> findById(int rsEventId);

    List<RsEvent> findAll();

    Long count();

    List<RsEvent> findLimit(Integer start, Integer end);

    RsEvent findOne(int index);

    void save(RsEvent rsEvent);

    void saveSelectively(RsEventPO rsEventPO);

    void deleteById(Integer index);
}
