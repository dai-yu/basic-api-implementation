package com.thoughtworks.rslist.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "userPO")
    private List<VotePO> votePOs;
}
