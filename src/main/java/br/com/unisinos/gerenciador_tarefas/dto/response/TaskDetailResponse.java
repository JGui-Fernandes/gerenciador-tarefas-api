package br.com.unisinos.gerenciador_tarefas.dto.response;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record TaskDetailResponse(
        Long id,
        String name,
        String description,
        Date deadline,
        TaskStatus status,
        ListUserResponse creator,
        ListUserResponse assignee,
        List<ListUserResponse> participants,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
