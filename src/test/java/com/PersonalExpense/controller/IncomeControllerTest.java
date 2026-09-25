package com.PersonalExpense.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class IncomeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private String registerAndLogin(String email, String password) throws Exception{
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\", \"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
        );
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
        .andReturn();

        JsonNode responseJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        return responseJson.get("token").asString();
    }

    @Test
    void updatingSomeoneElsesIncomeReturns403() throws Exception{
        String aliceToken = registerAndLogin("alice_test@test.com", "secret123");
        String bobToken = registerAndLogin("bob_test@test.com", "secret456");

        MvcResult createResult = mockMvc.perform(post("/income")
                .header("Authorization", "Bearer " + aliceToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Groceries\",\"amount\"100\",\"description\":\"shop\",\"source\":\"Job\",\"date\":\"2026-09-01\"}"))
                .andReturn();

        JsonNode created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long incomeId = created.get("id").asLong();

        mockMvc.perform(put("/income" + incomeId + "/update")
                .header("Authorization", "Bearer" + bobToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Hijacked\",\"amount\":\"1\",\"description\":\"x\",\"source\":\"Others\",\"date\":\"2026-09-01\"}"))
                .andExpect(status().isForbidden());
    }
}
