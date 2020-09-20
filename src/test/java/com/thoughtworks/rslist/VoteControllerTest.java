package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    ObjectMapper objectMapper;

    int rsEventId;
    UserPO userPO;
    @BeforeEach
    public void setUp(){
        userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        rsEventId = rsEventRepository.findAll().get(0).getId();
    }

    @AfterEach
    public void clear() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }
    @Test
    @Order(1)
    public void should_vote_correctly() throws Exception {
        Vote vote=Vote.builder().voteNum(1).userId(userPO.getId()).rsEventId(rsEventId)
                .voteTime(LocalDateTime.now()).build();
        String jsonString =objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1,rsEventRepository.findById(rsEventId).get().getVote());
        assertEquals(1,voteRepository.findByUserPO(userPO).getVoteNum());
        assertEquals(9,userRepository.findById(userPO.getId()).get().getVoteNum());
        assertEquals(userPO.getId(),voteRepository.findByUserPO(userPO).getUserPO().getId());
        assertEquals(vote.getVoteTime(),voteRepository.findByUserPO(userPO).getVoteTime());
    }

    @Test
    @Order(2)
    public void should_bad_request_when_vote_uncorrectly() throws Exception {
        Vote vote=Vote.builder().voteNum(20).userId(userPO.getId()).voteTime(LocalDateTime.now()).build();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void should_get_votes_for_a_specified_time_period() throws Exception {
        for (int i=1;i<9;i++){
            VotePO votePO = voteRepository.save(VotePO.builder().userPO(userPO).rsEventId(rsEventId).voteNum(1)
                    .voteTime(LocalDateTime.of(2020,9,i,6,0,0)).build());
            voteRepository.save(votePO);
        }
        String startTime = LocalDateTime.of(2020,9,1,1,0,0).toString();
        String endTime = LocalDateTime.of(2020,9,10,1,0,0).toString();
        mockMvc.perform(get("/vote")
                .param("startTime",startTime)
                .param("endTime",endTime)
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventId)))
                .andExpect(jsonPath("$[1].userId",is(userPO.getId())))
                .andExpect(jsonPath("$[2].voteNum",is(1)));
    }

    @Test
    @Order(4)
    public void should_get_votes_by_userId_and_rsEventId() throws Exception{
        for (int i=1;i<9;i++){
            VotePO votePO = voteRepository.save(VotePO.builder().userPO(userPO).rsEventId(rsEventId).voteNum(1)
                    .voteTime(LocalDateTime.of(2020,9,i,6,0,0)).build());
            voteRepository.save(votePO);
        }
        mockMvc.perform(get("/vote")
                .param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventId))
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventId)))
                .andExpect(jsonPath("$[1].userId",is(userPO.getId())))
                .andExpect(jsonPath("$[2].voteNum",is(1)));

    }
}
