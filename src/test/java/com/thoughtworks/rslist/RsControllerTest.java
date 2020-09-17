package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @AfterEach
    public void clear() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_get_rsevent_list() throws Exception {
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_one_rsevent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.name", is("第一件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))));
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.name", is("第二件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))));
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.name", is("第三件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))));
    }

    @Test
    @Order(3)
    public void should_get_rsevent_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))));
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第二件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第三件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))));
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))));

    }

    @Test
    @Order(4)
    public void should_add_rsevent() throws Exception {
        UserPO userPO = UserPO.builder().name("dave").age(22).email("abc@123.com").gender("male").phone("18888888888").build();
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
    @Order(5)
    public void should_modify_a_rsevent_has_name_and_keyword() throws Exception {
        RsEvent rsEvent = new RsEvent("新头条", "国际");
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("新头条")))
                .andExpect(jsonPath("$[0].keyword", is("国际")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void should_modify_a_rsevent_has_name_but_keyword() throws Exception {
        RsEvent rsEvent = new RsEvent("新头条", "");
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("新头条")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void should_modify_a_rsevent_has_keyword_but_name() throws Exception {
        RsEvent rsEvent = new RsEvent("", "国际");
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("国际")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void should_delete_rsevent_by_index() throws Exception {
        mockMvc.perform(delete("/rs/3"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void should_determine_user_not_exists() throws Exception {
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生", 1);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(3)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/user"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[1].user_name", is("dave")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void should_determine_user_exists() throws Exception {
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生", 1);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(3)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/user"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    public void should_throw_rsEvent_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    @Order(12)
    public void should_throw_method_argument_not_valid_exception() throws Exception {
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生", 1);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invaild param")))
                .andExpect(status().isBadRequest());
    }

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
    public void should_delete_user() throws Exception {
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
}
