package br.com.unisinos.gerenciador_tarefas.dto.request;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.util.Date;
import java.util.List;

public record CreateTaskRequest(
        String name,
        String description,
        Date deadline,
        TaskStatus status,
        Long assigneeId,
        List<Long> participantsId
) {
}
