package br.com.unisinos.gerenciador_tarefas.dto.response.task;

import br.com.unisinos.gerenciador_tarefas.entities.Task;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

public record ListTaskResponse(
        Long id,
        String name,
        TaskStatus status
) {

    public ListTaskResponse(Task t){
        this(t.getId(), t.getName(), t.getStatus());
    }
}
