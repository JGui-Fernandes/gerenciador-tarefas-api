package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.TaskDetailResponse;

import java.util.List;

public interface TaskService {

    TaskDetailResponse create(CreateTaskRequest request);

    TaskDetailResponse findById(Long id);

    List<ListTaskResponse> findAllAssignedTo(Long assignedTo);

    TaskDetailResponse update(
            Long id,
            CreateTaskRequest request
    );

    void delete(Long id);
}