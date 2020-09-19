package com.thoughtworks.rslist;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.Timestamp;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @AfterEach
    public void clear() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_get_votes_for_a_specified_time_period() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        voteRepository.save(VotePO.builder().userPO(userPO).rsEventId(rsEventId).voteNum(1)
                .voteTime(new Timestamp(System.currentTimeMillis())).build());
        voteRepository.save(VotePO.builder().userPO(userPO).rsEventId(rsEventId).voteNum(1)
                .voteTime(new Timestamp(System.currentTimeMillis())).build());
        voteRepository.save(VotePO.builder().userPO(userPO).rsEventId(rsEventId).voteNum(1)
                .voteTime(new Timestamp(System.currentTimeMillis())).build());
        mockMvc.perform(get("/vote").param("startTime","2020-09-01").param("endTime","2020-09-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventId)))
                .andExpect(jsonPath("$[1].userId",is(userPO.getId())))
                .andExpect(jsonPath("$[2].voteNum",is(1)));
    }

}
