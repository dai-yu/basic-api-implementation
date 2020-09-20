package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyword;
    @NotNull
    private int userId;

}
