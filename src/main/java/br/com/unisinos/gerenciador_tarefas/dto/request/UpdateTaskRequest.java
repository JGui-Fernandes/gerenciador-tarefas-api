package br.com.unisinos.gerenciador_tarefas.dto.request;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdateTaskRequest(
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        Long assigneeId
) {
}
