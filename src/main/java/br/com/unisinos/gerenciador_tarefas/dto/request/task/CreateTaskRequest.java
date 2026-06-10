package br.com.unisinos.gerenciador_tarefas.dto.request.task;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateTaskRequest(
        @NotNull(message = ErrorMessages.EMPTY_NAME)
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        Long assigneeId,
        List<Long> participantsId
) {
}
