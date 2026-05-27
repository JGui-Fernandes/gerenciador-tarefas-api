package br.com.unisinos.gerenciador_tarefas.dto.response;

import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

public record ListTaskResponse(
        Long id,
        String name,
        TaskStatus status
) {
}
