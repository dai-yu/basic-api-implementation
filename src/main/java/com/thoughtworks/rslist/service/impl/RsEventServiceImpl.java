package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<RsEvent> findAll() {
        return convertRsEvenPOtToRsEvent(rsEventRepository.findAll());
    }

    @Override
    public Long count() {
        return rsEventRepository.count();
    }

    @Override
    public List<RsEvent> findLimit(Integer start, Integer end) {
        return convertRsEvenPOtToRsEvent(rsEventRepository.findLimit(start,end));
    }

    @Override
    public RsEvent findOne(int index) {
        RsEventPO rsEventPO = rsEventRepository.findOne(index);
        RsEvent rsEvent = RsEvent.builder().userId(rsEventPO.getUserPO().getId())
                .eventName(rsEventPO.getEventName())
                .keyword(rsEventPO.getKeyWord())
                .build();
        return rsEvent;
    }

    @Override
    @Transactional
    public void save(RsEvent rsEvent) {
        Optional<UserPO> userPOOptional = userRepository.findById(rsEvent.getUserId());
        if (!userPOOptional.isPresent())throw new RsEventNotValidException("User does not exist");
        RsEventPO rsEventPO=RsEventPO.builder().eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyword()).userPO(userPOOptional.get()).build();
        rsEventRepository.save(rsEventPO);
    }

    @Override
    public void saveSelectively(RsEventPO rsEventPO) {
        rsEventRepository.save(rsEventPO);
    }

    @Override
    @Transactional
    public void deleteById(Integer index) {
        RsEventPO rsEventPO = rsEventRepository.findOne(index - 1);
        if (rsEventPO==null) throw new RsEventNotValidException("RsEvent does not exist,index is incorrect");
        rsEventRepository.deleteById(rsEventPO.getId());
    }

    public List<RsEvent> convertRsEvenPOtToRsEvent(List<RsEventPO> rsEventPOs){
        return rsEventPOs
                .stream()
                .map(rsEventPO -> RsEvent.builder()
                        .eventName(rsEventPO.getEventName())
                        .keyword(rsEventPO.getKeyWord())
                        .userId(rsEventPO.getUserPO().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
