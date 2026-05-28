package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.constants.Endpoints;
import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.constants.JsonPath;
import br.com.unisinos.gerenciador_tarefas.dto.request.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.mocks.request.TaskBody;
import br.com.unisinos.gerenciador_tarefas.mocks.response.ErrorMock;
import br.com.unisinos.gerenciador_tarefas.mocks.response.TaskMock;
import br.com.unisinos.gerenciador_tarefas.service.TaskService;
import br.com.unisinos.gerenciador_tarefas.util.JsonUtils;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Test
    void shouldCreateTask() throws Exception {

        CreateTaskRequest request =
                TaskBody.createTaskFullBody();

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
    void shouldReturnTask() throws Exception {

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
    void shouldReturnTaskNotFoundById() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundById();

        when(service.findAllAssignedTo(1L))
                .thenThrow(
                        new TaskNotFoundException(
                                error.message()
                        )
                );

        mockMvc.perform(get(Endpoints.TASKS + "?assignedTo=1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_ID));
    }

    @Test
    void shouldReturnTaskNotFoundByUser() throws Exception {

        ErrorMessageResponse error =
                ErrorMock.taskNotFoundByUser();

        when(service.findAllAssignedTo(1L))
                .thenThrow(
                        new TaskNotFoundException(
                                error.message()
                        )
                );

        mockMvc.perform(get(Endpoints.TASKS + "?assignedTo=1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonPath.ERROR_STATUS_CODE)
                        .value(error.statusCode()))
                .andExpect(jsonPath(JsonPath.ERROR_MESSAGE)
                        .value(ErrorMessages.TASK_NOT_FOUND_BY_USER));
    }
}