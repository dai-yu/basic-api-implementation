package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
//    }

    @Test
    public void should_get_rsevent_list() throws Exception {
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[2]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rsevent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.name", is("第一件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.name", is("第二件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.name", is("第三件事")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
    }

    @Test
    public void should_get_rsevent_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))));
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第二件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第三件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))));
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[2]",not(hasKey("user"))));

    }

    @Test
    public void should_add_rsevent() throws Exception {
        User user=new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[3].name", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
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
    public void should_determine_user_not_exists() throws Exception {
        User user=new User("dave","female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生",user);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",is(3)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/user"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].uesr_name",is("xiaowang")))
                .andExpect(jsonPath("$[1].user_name",is("dave")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_determine_user_exists() throws Exception {
        User user=new User("xiaowang","male",19,"b@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生",user);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",is(3)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/user"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].user_name",is("xiaowang")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_rsEvent_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    public void should_throw_method_argument_not_valid_exception() throws Exception {
        User user=new User("dave","female",15,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("dave的热搜", "民生",user);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invaild param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_rsEvent_not_valid_param_exception() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }

}
