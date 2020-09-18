package com.thoughtworks.rslist.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {

    private int userId;

    private int rsEventId;

    private int voteNum;
    private Date voteTime;
}
