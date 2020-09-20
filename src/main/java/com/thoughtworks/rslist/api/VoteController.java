package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/vote")
    public ResponseEntity<List<Vote>> getVotesByDate(@RequestParam(required = false) String startTime,
                                                     @RequestParam(required = false) String endTime,
                                                     @RequestParam(required = false) Integer rsEventId,
                                                     @RequestParam(required = false) Integer userId,
                                                     @RequestParam Integer pageIndex){
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        if (startTime != null && endTime != null) {
            return ResponseEntity.ok(voteRepository.findAllByDate(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime),pageable).stream().map(
                    item -> Vote.builder()
                            .userId(item.getUserPO().getId())
                            .rsEventId(item.getRsEventId())
                            .voteNum(item.getVoteNum())
                            .voteTime(item.getVoteTime()).build()
                    ).collect(Collectors.toList())
            );
        }
        if (rsEventId!=null && userId!=null ){
            List<VotePO> votePOList = voteRepository.findByRsEventIdAndUseIdINPage(rsEventId, userId, pageable);
            return ResponseEntity.ok(votePOList.stream().map(
                    item ->  Vote.builder()
                            .userId(item.getUserPO().getId())
                            .rsEventId(item.getRsEventId())
                            .voteNum(item.getVoteNum())
                            .voteTime(item.getVoteTime()).build()
                    ).collect(Collectors.toList())
            );
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/rs/vote/{rsEventId}")
    @Transactional
    public ResponseEntity voteByRsEventId(@PathVariable int rsEventId, @RequestBody Vote vote){
        Optional<RsEventPO> rsEventPOOptional = rsEventRepository.findById(rsEventId);
        Optional<UserPO> userPOOptional = userRepository.findById(vote.getUserId());
        UserPO userPO=userPOOptional.get();
        VotePO votePO = VotePO.builder().voteTime(vote.getVoteTime())
                .rsEventId(vote.getRsEventId()).userPO(userPOOptional.get()).voteNum(vote.getVoteNum()).build();
        if (!userPOOptional.isPresent() ||
                !rsEventPOOptional.isPresent() ||
                userPOOptional.get().getVoteNum()<vote.getVoteNum()){
            return ResponseEntity.badRequest().build();
        }
        rsEventPOOptional.get().setVote(rsEventPOOptional.get().getVote()+vote.getVoteNum());
        rsEventRepository.save(rsEventPOOptional.get());
        voteRepository.save(votePO);
        userPO.setVoteNum(userPOOptional.get().getVoteNum()-vote.getVoteNum());
        userRepository.save(userPO);
        return ResponseEntity.ok().build();
    }
}
