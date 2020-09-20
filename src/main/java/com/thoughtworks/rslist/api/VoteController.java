package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.VoteIndexNotValidException;
import com.thoughtworks.rslist.exception.VoteNotValidParamException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class VoteController {

    @Autowired
    UserService userService;
    @Autowired
    RsEventService rsEventService;
    @Autowired
    VoteService voteService;

    @GetMapping("/vote")
    public ResponseEntity<List<Vote>> getVotesByDate(@RequestParam(required = false) String startTime,
                                                     @RequestParam(required = false) String endTime,
                                                     @RequestParam(required = false) Integer rsEventId,
                                                     @RequestParam(required = false) Integer userId,
                                                     @RequestParam Integer pageIndex){
        if (pageIndex<1){
            throw new VoteIndexNotValidException("invalid index");
        }
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        List<Vote> votes;
        if (startTime != null && endTime != null) {
            votes=voteService.findVoteByDate(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime),pageable);
            return ResponseEntity.ok(votes);
        }
        if (rsEventId!=null && userId!=null ){
            votes=voteService.findByRsEventIdAndUseIdINPage(rsEventId, userId, pageable);
            return ResponseEntity.ok(votes);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteByRsEventId(@PathVariable int rsEventId, @RequestBody @Valid Vote vote){
        Optional<RsEventPO> rsEventPOOptional = rsEventService.findById(rsEventId);
        Optional<UserPO> userPOOptional = userService.findById(vote.getUserId());
        UserPO userPO=userPOOptional.get();
        VotePO votePO = VotePO.builder().voteTime(vote.getVoteTime())
                .rsEventId(vote.getRsEventId()).userPO(userPOOptional.get()).voteNum(vote.getVoteNum()).build();
//        if (!userPOOptional.isPresent() ||
//                !rsEventPOOptional.isPresent() ||
//                userPOOptional.get().getVoteNum()<vote.getVoteNum()){
//            throw new VoteNotValidParamException("invalid param");
//        }
        if (!userPOOptional.isPresent()) throw new VoteNotValidParamException("User does not exist");
        if (!rsEventPOOptional.isPresent()) throw new VoteNotValidParamException("RsEvent does not exist");
        if (userPOOptional.get().getVoteNum()<vote.getVoteNum()) throw new VoteNotValidParamException("Incorrect number of votes");
        rsEventPOOptional.get().setVote(rsEventPOOptional.get().getVote()+vote.getVoteNum());
        userPO.setVoteNum(userPOOptional.get().getVoteNum()-vote.getVoteNum());
        voteService.voteToRsEvent(rsEventPOOptional.get(),votePO,userPO);
        return ResponseEntity.ok().build();
    }
}
