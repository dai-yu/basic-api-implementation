package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.po.RsEventPO;

import java.util.Optional;

public interface RsEventService {
    Optional<RsEventPO> findById(int rsEventId);

}
