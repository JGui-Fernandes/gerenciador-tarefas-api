package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.constants.JsonPath;
import br.com.unisinos.gerenciador_tarefas.dto.request.auth.LoginRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.exception.InvalidCredentialsException;
import br.com.unisinos.gerenciador_tarefas.exception.BadRequestException;
import br.com.unisinos.gerenciador_tarefas.mocks.request.LoginBody;
import br.com.unisinos.gerenciador_tarefas.mocks.response.AuthMock;
import br.com.unisinos.gerenciador_tarefas.mocks.response.ErrorMock;
import br.com.unisinos.gerenciador_tarefas.service.AuthServiceImpl;
import br.com.unisinos.gerenciador_tarefas.util.JsonUtils;
import br.com.unisinos.gerenciador_tarefas.util.LoginTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServiceImpl authService;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = LoginBody.loginValidBody();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(AuthMock.tokenResponse());

        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedOnWrongPasswordError() throws Exception{
        LoginRequest request = LoginBody.loginValidBody();

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException(ErrorMessages.INVALID_LOGIN));

        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(401))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE).value(ErrorMessages.INVALID_LOGIN));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsMissingError() throws Exception {
        LoginRequest request = LoginBody.loginWithoutEmail();

        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnBadRequestWhenEmailIsInvalidError() throws Exception {
        LoginRequest request = LoginBody.loginWithInvalidEmail();

        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissingError() throws Exception{
        LoginRequest request = LoginBody.loginWithoutPassword();

        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenBodyIsEmptyError() throws Exception {
        mockMvc.perform(post(Endpoints.AUTH_LOGIN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {
        doNothing().when(authService).logout(any(String.class));

        mockMvc.perform(post(Endpoints.AUTH_LOGOUT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token-fake-valido"))

                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void shouldReturnUnauthorizedOnLogonWithoutTokenError() throws Exception {
        mockMvc.perform(post(Endpoints.AUTH_LOGOUT))
                .andExpect(status().isBadRequest());
    }
}
