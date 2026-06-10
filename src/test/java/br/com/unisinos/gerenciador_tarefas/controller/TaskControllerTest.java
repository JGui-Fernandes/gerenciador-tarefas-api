package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.constants.JsonPath;
import br.com.unisinos.gerenciador_tarefas.dto.request.task.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.task.UpdateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundByUserException;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.mocks.request.TaskBody;
import br.com.unisinos.gerenciador_tarefas.mocks.response.ErrorMock;
import br.com.unisinos.gerenciador_tarefas.mocks.response.TaskMock;
import br.com.unisinos.gerenciador_tarefas.service.TaskService;
import br.com.unisinos.gerenciador_tarefas.util.JsonUtils;
import br.com.unisinos.gerenciador_tarefas.util.LoginTestService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Autowired
    private LoginTestService login;

    // Cenário: Criar tarefa com todos os campos preenchidos com usuário logado.
    // Esperado: 201 Created (sem body na resposta).
    @Test
    void shouldCreateTaskWithAllAttributesSuccessfully() throws Exception {

        CreateTaskRequest request =
                TaskBody.createTaskFullBody();


        mockMvc.perform(post(Endpoints.TASKS)
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    // Cenário: Criar tarefa apenas com campos obrigatórios com usuário logado.
    // Esperado: 201 Created (sem body na resposta).
    @Test
    void shouldCreateTaskWithMandatoryAttributesSuccessfully() throws Exception {

        CreateTaskRequest request =
                TaskBody.createTaskMandatoryBody();


        mockMvc.perform(post(Endpoints.TASKS)
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }


    // Cenário: Tentar criar tarefa sem nenhum campo preenchido.
    // Esperado: 400 Bad Request.
    @Test
    void shouldNotCreateTaskWithNoAttributesError() throws Exception {
        mockMvc.perform(post(Endpoints.TASKS)
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // Cenário: Buscar tarefa por ID existente com usuário logado.
    // Esperado: 200 OK + TaskDetailResponse.
    @Test
    void shouldReturnTaskByIdSuccessfully() throws Exception {

        when(service.findById(1L))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(get(Endpoints.TASKS + "/1")
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_STATUS).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_CREATOR).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty());
    }

    // Cenário: Buscar tarefas atribuídas a um usuário existente.
    // Esperado: 200 OK + lista de ListTaskResponse.
    @Test
    void shouldReturnTaskByUserSuccessfully() throws Exception {

        List<ListTaskResponse> list =
                List.of(
                        TaskMock.listTaskResponse()
                );

        when(service.findAllAssignedTo(1L))
                .thenReturn(list);

        mockMvc.perform(
                        get(Endpoints.TASKS)
                                .param("assignedTo", "1")
                                .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
    }


    // Cenário: Buscar tarefa por ID que não existe.
    // Esperado: 404 Not Found.
    @Test
    void shouldReturnTaskNotFoundByIdError() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundById();

        when(service.findById(1L))
                .thenThrow(
                        new TaskNotFoundException()
                );

        mockMvc.perform(get(Endpoints.TASKS + "/1")
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_ID));
    }

    // Cenário: Buscar tarefas de um usuário que não existe.
    // Esperado: 404 Not Found.
    @Test
    void shouldReturnTaskNotFoundByUserError() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundByUser();

        when(service.findAllAssignedTo(1L))
                .thenThrow(
                        new TaskNotFoundByUserException()
                );

        mockMvc.perform(
                get(Endpoints.TASKS)
                        .param("assignedTo", "1")
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_USER));
    }

    // Cenário: Atualizar todos os campos de uma tarefa existente com usuário logado.
    // Esperado: 200 OK + TaskDetailResponse com os dados atualizados.
    @Test
    void shouldUpdateAllAttributesSuccessfully() throws Exception {
        UpdateTaskRequest request =
                TaskBody.updateTaskFullBody();

        when(service.update(
                eq(1L),
                any(UpdateTaskRequest.class)
                    ))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(put(Endpoints.TASKS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request))
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).value(request.name()))
                .andExpect(jsonPath(JsonPath.TASK_DESCRIPTION).value(request.description()))
                .andExpect(jsonPath(JsonPath.TASK_STATUS).value(request.status().toString()))
                .andExpect(jsonPath(JsonPath.TASK_CREATOR).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_ASSIGNEE).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_PARTICIPANTS).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_DEADLINE).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())

        ;
    }

    // Cenário: Atualizar apenas o nome de uma tarefa existente com usuário logado.
    // Esperado: 200 OK + TaskDetailResponse com o nome atualizado.
    @Test
    void shouldUpdateNameSuccessfully() throws Exception {
        UpdateTaskRequest request =
                TaskBody.updateName();

        when(service.update(
                eq(1L),
                any(UpdateTaskRequest.class)
        ))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(put(Endpoints.TASKS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request))
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())
        ;
    }

    // Cenário: Deletar tarefa existente com usuário logado.
    // Esperado: 204 No Content (sem body na resposta).

    @Test
    void shouldDeleteTaskByIdSuccessfully() throws Exception {
        mockMvc.perform(delete(Endpoints.TASKS + "/1")
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    // Cenário: Deletar tarefa que não existe.
    // Esperado: 404 Not Found.

    @Test
    void shouldReturnTaskNotFoundOnDeleteError() throws Exception {
        ErrorMessageResponse error = ErrorMock.taskNotFoundById();

        doThrow(new TaskNotFoundException())
                .when(service).delete(1L);

        mockMvc.perform(delete(Endpoints.TASKS + "/1")
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE).value(ErrorMessages.TASK_NOT_FOUND_BY_ID));
    }

    // Cenário: Tentar deletar tarefa sem estar logado.
    // Esperado: 401 Unauthorized.

    @Test
    void shouldReturnUnauthorizedOnDeleteTaskWithoutTokenError() throws Exception {
        mockMvc.perform(delete(Endpoints.TASKS + "/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE).value(401))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE).value(ErrorMessages.UNLOGGED_USER));
    }
}
