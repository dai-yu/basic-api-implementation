package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotVaildException;
import com.thoughtworks.rslist.exception.RsEventNotValidParamException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  VoteRepository voteRepository;

  private List<RsEvent> rsList = init();

  private List<RsEvent> init() {
    List<RsEvent> rsList=new ArrayList<>();
    rsList.add(new RsEvent("第一件事","无标签",1));
    rsList.add(new RsEvent("第二件事","无标签",2));
    rsList.add(new RsEvent("第三件事","无标签",3));
    return rsList;
  }

  @GetMapping("/rs/list")
  public ResponseEntity list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      return ResponseEntity.ok(rsList);
    }
    if (start <1 || end > rsList.size() || start>end){
      throw new RsEventNotValidParamException("invalid request param");
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity oneRsEvent(@PathVariable int index){
    if (index<=0 || index>rsList.size()){
      throw new RsEventNotVaildException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
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
    rsList.remove(index-1);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/rs/user")
  public ResponseEntity getUserList(){
    List<RsEventPO> all = rsEventRepository.findAll();
    return ResponseEntity.ok(all);
  }

  @PostMapping("/rs/vote/{rsEventId}")
  @Transactional
  public ResponseEntity voteByRsEventId(@PathVariable int rsEventId, @RequestBody VotePO votePO){
    RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
    Optional<UserPO> userPOOptional = userRepository.findById(votePO.getUserPO().getId());
    if (!userPOOptional.isPresent() || userPOOptional.get().getVoteNum()<votePO.getVoteNum()){
      return ResponseEntity.badRequest().build();
    }
    rsEventPO.setVote(rsEventPO.getVote()+votePO.getVoteNum());
    rsEventRepository.save(rsEventPO);
    voteRepository.save(votePO);
    return ResponseEntity.ok().build();
  }
}
