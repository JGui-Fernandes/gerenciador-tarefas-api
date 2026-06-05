package br.com.unisinos.gerenciador_tarefas.dto.request.task;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        Long assigneeId
) {
}
