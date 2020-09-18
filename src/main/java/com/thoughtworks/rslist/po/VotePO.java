package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

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

    @JoinColumn
    private int rsEventId;

    private int voteNum;
    private Date voteTime;

}
