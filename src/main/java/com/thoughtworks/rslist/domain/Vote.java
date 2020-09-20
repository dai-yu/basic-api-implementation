package com.thoughtworks.rslist.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {

    @NotNull
    private int userId;

    @NotNull
    private int rsEventId;

    @NotNull
    @Min(1)
    private int voteNum;

    private LocalDateTime voteTime;
}
