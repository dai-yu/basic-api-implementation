package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.News;
import org.apache.logging.log4j.message.ObjectMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_get_news_list() throws Exception {
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_new() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.name", is("第一件事")))
                .andExpect(jsonPath("$.type", is("无标签")));
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.name", is("第二件事")))
                .andExpect(jsonPath("$.type", is("无标签")));
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.name", is("第三件事")))
                .andExpect(jsonPath("$.type", is("无标签")));
    }

    @Test
    public void should_get_news_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")));
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("第二件事")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第三件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")));
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")));

    }

    @Test
    public void should_add_news() throws Exception {
        News news=new News("猪肉涨价了","经济");
        String jsonString=new ObjectMapper().writeValueAsString(news);
        mockMvc.perform(post("/rs/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")))
                .andExpect(jsonPath("$[3].name", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].type", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_modify_a_news_has_name_and_type() throws Exception {
        News news=new News("新头条","国际");
        String jsonString=new ObjectMapper().writeValueAsString(news);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("新头条")))
                .andExpect(jsonPath("$[0].type", is("国际")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_modify_a_news_has_name_but_type() throws Exception {
        News news=new News("新头条","");
        String jsonString=new ObjectMapper().writeValueAsString(news);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("新头条")))
                .andExpect(jsonPath("$[0].type", is("无标签")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_modify_a_news_has_type_but_name() throws Exception {
        News news=new News("","国际");
        String jsonString=new ObjectMapper().writeValueAsString(news);
        mockMvc.perform(put("/rs/modify?index=1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("第一件事")))
                .andExpect(jsonPath("$[0].type", is("国际")))
                .andExpect(jsonPath("$[1].name", is("第二件事")))
                .andExpect(jsonPath("$[1].type", is("无标签")))
                .andExpect(jsonPath("$[2].name", is("第三件事")))
                .andExpect(jsonPath("$[2].type", is("无标签")))
                .andExpect(status().isOk());
    }
}
