package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidParamException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

  RsEventService rsEventService;
  UserService userService;

  public RsController(RsEventService rsEventService, UserService userService) {
    this.rsEventService = rsEventService;
    this.userService = userService;
  }

  @GetMapping("/rs/list")
  public ResponseEntity list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      List<RsEvent> all = rsEventService.findAll();
      return ResponseEntity.ok(all);
    }
    if (start <1 || end > rsEventService.count() || start>end){
      throw new RsEventNotValidParamException("invalid request param");
    }
    List<RsEvent> all = rsEventService.findLimit(start-1,end);
    return ResponseEntity.ok(all);
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index){
    if (index<=0 || index> rsEventService.count()){
      throw new RsEventNotValidException("invalid index");
    }
    RsEvent rsEvent = rsEventService.findOne(index - 1);
    return ResponseEntity.ok(rsEvent);
  }

  @PostMapping("/rs/event")
  public ResponseEntity add(@RequestBody @Valid RsEvent rsEvent){
    rsEventService.save(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{rsEventId}")
  @Transactional
  public ResponseEntity modify(@PathVariable Integer rsEventId,@RequestBody RsEvent rsEvent){
    Optional<RsEventPO> rsEventPOOptional = rsEventService.findById(rsEventId);
    if (rsEvent.getUserId()!=rsEventPOOptional.get().getUserPO().getId() || !rsEventPOOptional.isPresent()){
      throw  new RsEventNotValidException("RsEvent is incorrect");
    }
    RsEventPO rsEventPO = RsEventPO.builder()
            .eventName(rsEvent.getEventName()!=null?rsEvent.getEventName():rsEventPOOptional.get().getEventName())
            .keyWord(rsEvent.getKeyword()!=null?rsEvent.getKeyword():rsEventPOOptional.get().getKeyWord())
            .userPO(UserPO.builder().id(rsEvent.getUserId()).build())
            .id(rsEventId)
            .build();
    rsEventService.saveSelectively(rsEventPO);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity delete(@PathVariable Integer index){
    rsEventService.deleteById(index);
    return ResponseEntity.ok(null);
  }


}
