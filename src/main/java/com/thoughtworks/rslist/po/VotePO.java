package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity(name = "Vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePO {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private UserPO userPO;

    private int rsEventId;

    private int voteNum;

    private LocalDateTime voteTime;

}
