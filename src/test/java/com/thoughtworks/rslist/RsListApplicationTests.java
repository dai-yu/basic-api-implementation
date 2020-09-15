package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].name",is("第一件事")))
                .andExpect(jsonPath("$[0].type",is("无标签")))
                .andExpect(jsonPath("$[1].name",is("第二件事")))
                .andExpect(jsonPath("$[1].type",is("无标签")))
                .andExpect(jsonPath("$[2].name",is("第三件事")))
                .andExpect(jsonPath("$[2].type",is("无标签")))
                .andExpect(status().isOk());
    }


}
