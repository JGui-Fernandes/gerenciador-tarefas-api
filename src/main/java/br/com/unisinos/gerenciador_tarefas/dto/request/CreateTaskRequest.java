package br.com.unisinos.gerenciador_tarefas.dto.request;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateTaskRequest(
        @NotNull(message = "O campo name é um campo obrigatório")
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        @NotNull (message = "O campo creatorId é obrigatório")
        Long creatorId,
        Long assigneeId,
        List<Long> participantsId
) {
}
