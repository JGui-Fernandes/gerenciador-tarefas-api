package br.com.unisinos.gerenciador_tarefas.util;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.dto.request.auth.LoginRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.auth.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class LoginTestService {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public String loginSuccessful() throws Exception {

        LoginRequest request = new LoginRequest(
                "admin@email.com",
                "123456"
        );

        MvcResult result = mockMvc.perform(
                        post(Endpoints.AUTH_LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
                result.getResponse().getContentAsString();

        LoginResponse response =
                objectMapper.readValue(
                        responseBody,
                        LoginResponse.class
                );

        return "Bearer " + response.token();
    }

}