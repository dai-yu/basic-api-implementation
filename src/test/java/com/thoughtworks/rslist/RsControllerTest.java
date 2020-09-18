package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
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
    public void should_get_rsEvent_list() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        for (int i=1;i<4;i++){
            RsEventPO rsEventPO = RsEventPO.builder().eventName("第" + i + "件事").keyWord("无标签").userPO(userPO).build();
            rsEventRepository.save(rsEventPO);
        }
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第1件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第2件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第3件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_one_rsEvent() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        for (int i=1;i<4;i++){
            RsEventPO rsEventPO = RsEventPO.builder().eventName("第" + i + "件事").keyWord("无标签").userPO(userPO).build();
            rsEventRepository.save(rsEventPO);
        }
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第1件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第2件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第3件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    @Order(3)
    public void should_get_rsEvent_between() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        for (int i=1;i<4;i++){
            RsEventPO rsEventPO = RsEventPO.builder().eventName("第" + i + "件事").keyWord("无标签").userPO(userPO).build();
            rsEventRepository.save(rsEventPO);
        }
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第1件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第2件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第2件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第3件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第1件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第2件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第3件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")));

    }

    @Test
    @Order(4)
    public void should_add_rsEvent() throws Exception {
        UserPO userPO = UserPO.builder().name("dave").age(22).email("abc@123.com").gender("male").phone("18888888888").build();
        userRepository.save(userPO);
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":" + userPO.getId() + "}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals("猪肉涨价了", all.get(0).getEventName());
        assertEquals("经济", all.get(0).getKeyWord());
    }


    @Test
    @Order(8)
    public void should_delete_rsEvent_by_index() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        for (int i=1;i<4;i++){
            RsEventPO rsEventPO = RsEventPO.builder().eventName("第" + i + "件事").keyWord("无标签").userPO(userPO).build();
            rsEventRepository.save(rsEventPO);
        }
        mockMvc.perform(delete("/rs/3"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第1件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第2件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(status().isOk());
    }


    @Test
    @Order(11)
    public void should_throw_rsEvent_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

//    @Test
//    @Order(12)
//    public void should_throw_method_argument_not_valid_exception() throws Exception {
//        RsEvent rsEvent = new RsEvent("dave的热搜", "民生", 1);
//        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.error", is("invaild param")))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @Order(13)
    public void should_throw_rsEvent_not_valid_param_exception() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    @Order(14)
    public void should_BadRequest_when_user_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":100}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    public void should_delete_user_and_users_all_rsEvent() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        mockMvc.perform(delete("/user/{id}", userPO.getId()))
                .andExpect(status().isOk());
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }

    @Test
    @Order(16)
    public void should_update_rsEvent() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        int userId = userRepository.findAll().get(0).getId();
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();

        String jsonString = "{\"eventName\":\"新的热搜\",\"keyword\":\"新的关键词\",\"userId\":" + userId + "}";
        mockMvc.perform(patch("/rs/" + rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("新的热搜", rsEventRepository.findAll().get(0).getEventName());
        assertEquals("新的关键词", rsEventRepository.findAll().get(0).getKeyWord());
        assertEquals(userId, rsEventRepository.findAll().get(0).getUserPO().getId());
    }

    @Test
    @Order(16)
    public void should_bad_request_when_userId_and_rsEventId_mismatch() throws Exception{
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        int userId = userRepository.findAll().get(0).getId();
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        String jsonString = "{\"eventName\":\"新的热搜\",\"keyword\":\"新的关键词\",\"userId\":" + userId+1 + "}";
        mockMvc.perform(patch("/rs/" + rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    public void should_only_update_eventName_when_only_have_eventName() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        int userId = userRepository.findAll().get(0).getId();
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        String jsonString = "{\"eventName\":\"新的热搜\",\"userId\":" + userId + "}";
        mockMvc.perform(patch("/rs/" + rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("新的热搜", rsEventRepository.findAll().get(0).getEventName());
        assertEquals("经济", rsEventRepository.findAll().get(0).getKeyWord());
        assertEquals(userId, rsEventRepository.findAll().get(0).getUserPO().getId());
    }

    @Test
    @Order(18)
    public void should_only_update_keyWord_when_only_have_keyWord() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        int userId = userRepository.findAll().get(0).getId();
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        String jsonString = "{\"keyword\":\"新的关键词\",\"userId\":" + userId+ "}";
        mockMvc.perform(patch("/rs/" + rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("涨工资了", rsEventRepository.findAll().get(0).getEventName());
        assertEquals("新的关键词", rsEventRepository.findAll().get(0).getKeyWord());
        assertEquals(userId, rsEventRepository.findAll().get(0).getUserPO().getId());
    }

    @Test
    @Order(19)
    public void should_vote_correctly() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        Vote vote=Vote.builder().voteNum(5).userId(userPO.getId()).voteTime(new Date(System.currentTimeMillis())).build();
        String jsonString = new ObjectMapper().writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(5,rsEventRepository.findById(rsEventId).get().getVote());
        assertEquals(5,voteRepository.findAll().get(0).getVoteNum());
        assertEquals(userPO.getId(),voteRepository.findAll().get(0).getUserPO().getId());
    }

    @Test
    @Order(19)
    public void should_bad_request_when_vote_uncorrectly() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        Vote vote=Vote.builder().voteNum(20).userId(userPO.getId()).voteTime(new Date(System.currentTimeMillis())).build();
        String jsonString = new ObjectMapper().writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
