package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/vote")
    public ResponseEntity<List<Vote>> getVotesByDate(@RequestParam String startTime, @RequestParam String endTime){

        return ResponseEntity.ok(voteRepository.findAllByDate(LocalDateTime.parse(startTime),LocalDateTime.parse(endTime)).stream().map(
                item -> Vote.builder()
                        .userId(item.getUserPO().getId())
                        .rsEventId(item.getRsEventId())
                        .voteNum(item.getVoteNum())
                        .voteTime(item.getVoteTime()).build()
        ).collect(Collectors.toList())
        );
    }
}
