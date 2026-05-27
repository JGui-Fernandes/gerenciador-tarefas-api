package br.com.unisinos.gerenciador_tarefas.mocks;

import br.com.unisinos.gerenciador_tarefas.dto.response.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;

public class TaskMock {

    public static TaskDetailResponse taskDetailResponse() {

        return new TaskDetailResponse(
                1L,
                "Criar documentação da API",
                "Documentar endpoints",
                LocalDate.now(),
                TaskStatus.IN_PROGRESS,
                UserMock.listUserResponse(),
                UserMock.listUserResponse(),
                List.of(
                        UserMock.listUserResponse()
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}