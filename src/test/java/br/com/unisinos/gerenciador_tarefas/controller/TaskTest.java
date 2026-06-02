package br.com.unisinos.gerenciador_tarefas.scenarios;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.constants.JsonPath;
import br.com.unisinos.gerenciador_tarefas.dto.request.task.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.ListTaskResponse;
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
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Autowired
    private LoginTestService login;

    @Test
    void shouldCreateTaskWithAllAttributesSuccessfully() throws Exception {

        CreateTaskRequest request =
                TaskBody.createTaskFullBody();

        when(service.create(any(CreateTaskRequest.class)))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(post(Endpoints.TASKS)
                        .header(HttpHeaders.AUTHORIZATION, login.loginSuccessful())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    void shouldCreateTaskWithMandatoryAttributesSuccessfully() throws Exception {

        CreateTaskRequest request =
                TaskBody.createTaskMandatoryBody();

        when(service.create(any(CreateTaskRequest.class)))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(post(Endpoints.TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    @Test
    void shouldNotCreateTaskWithNoAttributesError() throws Exception {

        mockMvc.perform(post(Endpoints.TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnTaskByIdSuccessfully() throws Exception {

        when(service.findById(1L))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(get(Endpoints.TASKS + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JsonPath.ID).isNumber())
                .andExpect(jsonPath(JsonPath.NAME).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_STATUS).isNotEmpty())
                .andExpect(jsonPath(JsonPath.TASK_CREATOR).isNotEmpty())
                .andExpect(jsonPath(JsonPath.CREATEDAT).isNotEmpty())
                .andExpect(jsonPath(JsonPath.UPDATEDAT).isNotEmpty());
    }

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
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
    }

    @Test
    void shouldReturnTaskNotFoundByIdError() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundById();

        when(service.findById(1L))
                .thenThrow(
                        new TaskNotFoundException(
                                error.message()
                        )
                );

        mockMvc.perform(get(Endpoints.TASKS + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_ID));
    }

    @Test
    void shouldReturnTaskNotFoundByUserError() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundByUser();

        when(service.findAllAssignedTo(1L))
                .thenThrow(
                        new TaskNotFoundException(
                                error.message()
                        )
                );

        mockMvc.perform(
                get(Endpoints.TASKS)
                        .param("assignedTo", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_USER));
    }

    @Test
    void shouldUpdateAllAttributesSuccessfully() throws Exception{
        CreateTaskRequest request =
                TaskBody.createTaskFullBody();

        when(service.update(
                eq(1L),
                any(CreateTaskRequest.class)
                    ))
                .thenReturn(
                        TaskMock.taskDetailResponse()
                );

        mockMvc.perform(put(Endpoints.TASKS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(request)))
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
}