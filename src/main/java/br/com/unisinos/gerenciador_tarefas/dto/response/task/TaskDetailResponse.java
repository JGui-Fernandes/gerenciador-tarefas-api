package br.com.unisinos.gerenciador_tarefas.dto.response.task;

import br.com.unisinos.gerenciador_tarefas.dto.response.user.ListUserResponse;
import br.com.unisinos.gerenciador_tarefas.entities.Task;
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
    public TaskDetailResponse(Task task){
        this(task.getId(), task.getName(), task.getDescription(), task.getDeadline(), task.getStatus(),
                new ListUserResponse(task.getCreator()), new ListUserResponse(task.getAssignee()),
                task.getParticipants().stream().map(ListUserResponse::new).toList(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
