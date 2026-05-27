package br.com.unisinos.gerenciador_tarefas.dto.request;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public record CreateTaskRequest(
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        Long assigneeId,
        List<Long> participantsId
) {
}
