package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @AfterEach
    public void clear(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user=new User("dave","male",22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("dave",all.get(0).getName());
        assertEquals(22,all.get(0).getAge());
    }

    @Test
    @Order(2)
    public void name_should_less_than_8() throws Exception {
        User user=new User("dave123456789","male",22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(3)
    public void gender_should_not_null() throws Exception {
        User user=new User("dave",null,22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(4)
    public void age_should_between_18_and_100() throws Exception {
        User user=new User("dave","male",15,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(5)
    public void email_should_suit_format() throws Exception {
        User user=new User("dave","male",22,"abc123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(6)
    public void phone_should_suit_format() throws Exception {
        User user=new User("dave","male",22,"abc@123.com","18888855588888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    public void should_throw_method_argument_not_valid_exception() throws Exception {
        User user=new User("dave","male",15,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    public void should_get_user_by_id() throws Exception {
        User user=new User("dave","male",22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> all = userRepository.findAll();
        mockMvc.perform(get("/user/"+all.get(0).getId()))
                .andExpect(jsonPath("$.name",is("dave")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void should_delete_user_by_id() throws Exception {
        User user=new User("dave","male",22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> all = userRepository.findAll();
        mockMvc.perform(delete("/user/"+all.get(0).getId()))
                .andExpect(status().isOk());
        assertEquals(false,userRepository.findById(all.get(0).getId()).isPresent());
    }

    @Test
    @Order(10)
    public void should_delete_user_and_users_all_votes() throws Exception{
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        userRepository.save(userPO);
        RsEventPO eventPO = RsEventPO.builder().eventName("涨工资了").keyWord("经济").userPO(userPO).build();
        rsEventRepository.save(eventPO);
        int rsEventId = rsEventRepository.findAll().get(0).getId();
        Vote vote=Vote.builder().voteNum(6).userId(userPO.getId()).voteTime(new Timestamp(System.currentTimeMillis())).build();
        String jsonString = new ObjectMapper().writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+eventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(6,rsEventRepository.findById(rsEventId).get().getVote());
        assertEquals(6,voteRepository.findAll().get(0).getVoteNum());
        assertEquals(userPO.getId(),voteRepository.findAll().get(0).getUserPO().getId());
        mockMvc.perform(delete("/user/"+userPO.getId()))
                .andExpect(status().isOk());
        assertEquals(0,rsEventRepository.count());
        assertEquals(0,voteRepository.count());
    }

    @Test
    @Order(11)
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
    @Order(12)
    public void should_get_all_users() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("19999999999").name("dave")
                .age(22).gender("male").email("abc@123.com").build();
        UserPO userPO2 = UserPO.builder().voteNum(10).phone("19998888899").name("xiaoming")
                .age(18).gender("female").email("666@123.com").build();
        userRepository.save(userPO);
        userRepository.save(userPO2);
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
        assertEquals(2,userRepository.findAll().size());
    }

}
