package br.com.unisinos.gerenciador_tarefas.dto.response;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailResponse(
        Long id,
        String name,
        String description,
        LocalDate deadline,
        TaskStatus status,
        ListUserResponse creator,
        ListUserResponse assignee,
        List<ListUserResponse> participants,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
