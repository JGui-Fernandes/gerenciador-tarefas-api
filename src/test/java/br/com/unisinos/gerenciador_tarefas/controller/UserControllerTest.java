package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.constants.JsonPath;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.UpdateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.exception.BadRequestException;
import br.com.unisinos.gerenciador_tarefas.exception.UserNotFoundException;
import br.com.unisinos.gerenciador_tarefas.mocks.request.UserBody;
import br.com.unisinos.gerenciador_tarefas.mocks.response.ErrorMock;
import br.com.unisinos.gerenciador_tarefas.mocks.response.UserMock;
import br.com.unisinos.gerenciador_tarefas.service.AuthServiceImpl;
import br.com.unisinos.gerenciador_tarefas.service.UserService;
import br.com.unisinos.gerenciador_tarefas.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doThrow;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private AuthServiceImpl authService;

    //Cenário: Criar usuário com todos os campos preenchidos.
    //Esperado: 201 Created + UserDetailResponse no body.

    @Test
    void shouldCreateUserWithAllAttributesSuccessfully() throws Exception{
        CreateUserRequest request = UserBody.createUserFullBody();

        mockMvc.perform(post(Endpoints.USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated());
    }


    //Cenário: Criar usuário apenas com campos obrigatórios (sem birthDate e phone).
    //Esperado: 201 Created.

    @Test
    void shouldCreateUserWithMandatoryAttributesSuccessfully() throws Exception {
        CreateUserRequest request = UserBody.createUserMandatoryBody();

        mockMvc.perform(post(Endpoints.USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated());
    }


    //Cenário: Tentar criar usuário com email duplicado.
    //Esperado: 400 Bad Request + mensagem de email em uso.

    @Test
    void shouldNotCreateUserWithDuplicateEmailError() throws Exception {
        CreateUserRequest request = UserBody.createUserFullBody();
        ErrorMessageResponse error = ErrorMock.duplicateEmail();


        doThrow(new BadRequestException(ErrorMessages.REUSED_EMAIL))
                .when(service).create(any(CreateUserRequest.class));

        mockMvc.perform(post(Endpoints.USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE).value(ErrorMessages.REUSED_EMAIL));
    }


    // Cenário: Buscar usuário por id existente com admin logado.
    // Esperado: 200 OK + UserDetailResponse.

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserByIdSuccessfully() throws Exception {
        when(service.findById(1L))
                .thenReturn(UserMock.userDetailResponse());
        mockMvc.perform(get(Endpoints.USERS + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).isNotEmpty())
                .andExpect(jsonPath(JsonPath.USER_EMAIL).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty());
    }

    // Cenário: Buscar usuário logado.
    // Esperado: 200 OK + UserDetailResponse.

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnLoggedInUserSuccessfully() throws Exception {
        when(service.detailLoggedUser())
                .thenReturn(UserMock.userDetailResponse());

        mockMvc.perform(get(Endpoints.USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).isNotEmpty())
                .andExpect(jsonPath(JsonPath.USER_EMAIL).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty());
    }

    // Cenário: Buscar usuário por id com User logado.
    // Esperado: 403 forbidden.

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotReturnUserByIdWithForbiddenError() throws Exception {
        when(service.findById(1L))
                .thenReturn(UserMock.userDetailResponse());
        mockMvc.perform(get(Endpoints.USERS + "/1"))
                .andExpect(status().isForbidden());
    }

    // Cenário: Listar usuários ativos com Admin logado.
    // Esperado: 200 OK + lista ListUserResponse.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserListSuccessfully() throws Exception {
        when(service.listActiveUsers())
                .thenReturn(UserMock.listUserResponseList());

        mockMvc.perform(get(Endpoints.USERS + "/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    // Cenário: Listar usuários ativos com User logado.
    // Esperado: 403 Forbidden.
    @Test
    @WithMockUser(roles = "USER")
    void shouldNotReturnUserListWithForbiddenError() throws Exception {
        when(service.listActiveUsers())
                .thenReturn(UserMock.listUserResponseList());

        mockMvc.perform(get(Endpoints.USERS + "/list"))
                .andExpect(status().isForbidden());
    }

    //Cenário: Atualizar usuário que não existe.
    //Esperado: 404 Not Found.

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserNotFoundOnUpdateError() throws Exception{
        CreateUserRequest request = UserBody.createUserFullBody();
        ErrorMessageResponse error = ErrorMock.userNotFoundById();

        doThrow(new UserNotFoundException())
                .when(service).update(any(Long.class), any(UpdateUserRequest.class));

        mockMvc.perform(put(Endpoints.USERS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.USER_NOT_FOUND_BY_ID));


    }

    // Cenário: Atualiza usuário por id com User logado.
    // Esperado: 403 forbidden.
    @Test
    @WithMockUser(roles = "USER")
    void shouldNotUpdateUserByIdWithForbiddenError() throws Exception{
        CreateUserRequest request = UserBody.createUserFullBody();

        mockMvc.perform(put(Endpoints.USERS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isForbidden());

    }

    //Cenário: Deletar usuário porId com Admin logado.
    //Esperado: 204 No Content (sem body na resposta).

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUserByIdSuccessfully() throws Exception{
        mockMvc.perform(delete(Endpoints.USERS + "/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    //Cenário: Deletar usuário logado.
    //Esperado: 204 No Content (sem body na resposta).

    @Test
    @WithMockUser(roles = "USER")
    void shouldDeleteLoggedUserSuccessfully() throws Exception{
        mockMvc.perform(delete(Endpoints.USERS))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    //Cenário: Deletar usuário existente com User logado.
    //Esperado: 403 Forbidden.

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotDeleteUserByIdWithForbiddenError() throws Exception{
        mockMvc.perform(delete(Endpoints.USERS + "/1"))
                .andExpect(status().isForbidden());
    }

    //Cenário: Tentar deletar sem estar logado.
    //Esperado: 401 Unauthorized.

    @Test
    void shouldReturnUnauthorizedOnDeleteWithoutTokenError() throws Exception {
        mockMvc.perform(delete(Endpoints.USERS + "/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(401))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.UNLOGGED_USER));
    }

    //Cenário: Tentar deletar usuário que não existe.
    //Esperado: 404 Not Found.

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserNotFoundOnDeleteError() throws Exception {
        ErrorMessageResponse error = ErrorMock.userNotFoundById();

        doThrow(new UserNotFoundException())
                .when(service).delete(1L);

        mockMvc.perform(delete(Endpoints.USERS + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.USER_NOT_FOUND_BY_ID));
    }
}
