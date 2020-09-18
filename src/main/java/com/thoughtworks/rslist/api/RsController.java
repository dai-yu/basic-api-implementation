package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotVaildException;
import com.thoughtworks.rslist.exception.RsEventNotValidParamException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
//    if (index<=0 || index>rsList.size()){
//      throw new RsEventNotVaildException("invalid index");
//    }
//    return ResponseEntity.ok(rsList.get(index-1));
    return ResponseEntity.ok(null);
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
//    rsList.remove(index-1);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/rs/user")
  public ResponseEntity getUserList(){
    List<RsEventPO> all = rsEventRepository.findAll();
    return ResponseEntity.ok(all);
  }

  @PostMapping("/rs/vote/{rsEventId}")
  @Transactional
  public ResponseEntity voteByRsEventId(@PathVariable int rsEventId, @RequestBody Vote vote){
    Optional<RsEventPO> rsEventPOOptional = rsEventRepository.findById(rsEventId);
    Optional<UserPO> userPOOptional = userRepository.findById(vote.getUserId());
    VotePO votePO = VotePO.builder()
            .rsEventId(vote.getRsEventId()).userPO(userPOOptional.get()).voteNum(vote.getVoteNum()).build();
    if (!userPOOptional.isPresent() ||
            !rsEventPOOptional.isPresent() ||
            userPOOptional.get().getVoteNum()<vote.getVoteNum()){
      return ResponseEntity.badRequest().build();
    }
    rsEventPOOptional.get().setVote(rsEventPOOptional.get().getVote()+vote.getVoteNum());
    rsEventRepository.save(rsEventPOOptional.get());
    voteRepository.save(votePO);
    return ResponseEntity.ok().build();
  }
}
