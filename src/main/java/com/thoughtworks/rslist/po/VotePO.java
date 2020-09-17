package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @JoinColumn
    private int userId;
    @JoinColumn
    private int rsEventId;

    private int voteNum;
    private Date voteTime;
}
