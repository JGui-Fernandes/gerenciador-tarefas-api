package br.com.unisinos.gerenciador_tarefas.mocks.response;

import br.com.unisinos.gerenciador_tarefas.dto.response.ListUserResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaskMock {

    public static TaskDetailResponse taskDetailResponse() {

        ListUserResponse creator =
                new ListUserResponse(
                        1L,
                        "John",
                        "john@email.com"
                );

        return new TaskDetailResponse(
                1L,
                "Criar documentação da API",
                "Documentar endpoints",
                LocalDate.now(),
                TaskStatus.IN_PROGRESS,
                creator,
                creator,
                List.of(creator),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}