package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotVaildException;
import com.thoughtworks.rslist.exception.RsEventNotValidParamException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  VoteRepository voteRepository;


  @GetMapping("/rs/list")
  public ResponseEntity list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      List<RsEvent> all = rsEventRepository.findAll().stream().map(rsEventPO -> RsEvent.builder()
              .eventName(rsEventPO.getEventName())
              .keyword(rsEventPO.getKeyWord())
              .userId(rsEventPO.getUserPO().getId()).build()).collect(Collectors.toList());

      return ResponseEntity.ok(all);
    }
    if (start <1 || end > rsEventRepository.count() || start>end){
      throw new RsEventNotValidParamException("invalid request param");
    }
    List<RsEvent> all = rsEventRepository.findLimit(start-1,end).stream().map(rsEventPO -> RsEvent.builder()
            .eventName(rsEventPO.getEventName())
            .keyword(rsEventPO.getKeyWord())
            .userId(rsEventPO.getUserPO().getId()).build()).collect(Collectors.toList());
    return ResponseEntity.ok(all);
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity oneRsEvent(@PathVariable int index){
    if (index<=0 || index> rsEventRepository.count()){
      throw new RsEventNotVaildException("invalid index");
    }
    RsEventPO rsEventPO = rsEventRepository.findOne(index - 1);
    RsEvent rsEvent = RsEvent.builder().userId(rsEventPO.getUserPO().getId())
            .eventName(rsEventPO.getEventName())
            .keyword(rsEventPO.getKeyWord())
            .build();
    return ResponseEntity.ok(rsEvent);
  }

  @PostMapping("/rs/event")
  public ResponseEntity add(@RequestBody @Valid RsEvent rsEvent){
    Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
    if (!userPO.isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPO rsEventPO=RsEventPO.builder().eventName(rsEvent.getEventName())
            .keyWord(rsEvent.getKeyword()).userPO(userPO.get()).build();
    rsEventRepository.save(rsEventPO);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{rsEventId}")
  @Transactional
  public ResponseEntity modify(@PathVariable Integer rsEventId,@RequestBody RsEvent rsEvent){
    Optional<RsEventPO> rsEventPOExit = rsEventRepository.findById(rsEventId);
    if (rsEvent.getUserId()!=rsEventPOExit.get().getUserPO().getId() || !rsEventPOExit.isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPO rsEventPO = RsEventPO.builder()
            .eventName(rsEvent.getEventName()!=null?rsEvent.getEventName():rsEventPOExit.get().getEventName())
            .keyWord(rsEvent.getKeyword()!=null?rsEvent.getKeyword():rsEventPOExit.get().getKeyWord())
            .userPO(UserPO.builder().id(rsEvent.getUserId()).build())
            .id(rsEventId)
            .build();
    rsEventRepository.save(rsEventPO);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity delete(@PathVariable Integer index){
    RsEventPO rsEventPO = rsEventRepository.findOne(index - 1);
    rsEventRepository.deleteById(rsEventPO.getId());
    return ResponseEntity.ok(null);
  }

  @GetMapping("/rs/user")
  public ResponseEntity getUserList(){
    List<RsEventPO> all = rsEventRepository.findAll();
    return ResponseEntity.ok(all);
  }

}
