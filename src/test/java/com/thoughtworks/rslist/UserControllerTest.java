package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void clear(){
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user=new User("dave","male",22,"abc@123.com","18888888888");
        String jsonString=new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
//        mockMvc.perform(get("/user"))
//                .andExpect(jsonPath("$",hasSize(1)))
//                .andExpect(jsonPath("$[0].user_name",is("dave")))
//                .andExpect(jsonPath("$[0].user_gender",is("male")))
//                .andExpect(jsonPath("$[0].user_age",is(22)))
//                .andExpect(jsonPath("$[0].user_email",is("abc@123.com")))
//                .andExpect(jsonPath("$[0].user_phone",is("18888888888")))
//                .andExpect(status().isOk());
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
}
