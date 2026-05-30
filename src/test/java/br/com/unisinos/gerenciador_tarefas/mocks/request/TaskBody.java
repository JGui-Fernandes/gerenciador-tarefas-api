package br.com.unisinos.gerenciador_tarefas.mocks.request;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskBody {

    public static CreateTaskRequest createTaskFullBody(){
        List<Long> participantsIdList = new ArrayList<>();
        participantsIdList.add(1L);
        participantsIdList.add(3L);

        return new CreateTaskRequest(
                "Criar documentação da API",
                "Documentar endpoints",
                LocalDate.now(),
                TaskStatus.IN_PROGRESS,
                1L,
                2L,
                participantsIdList
        );
    }

    public static CreateTaskRequest createTaskMandatoryBody(){
        List<Long> participantsIdList = new ArrayList<>();
        participantsIdList.add(1L);
        participantsIdList.add(3L);

        return new CreateTaskRequest(
                "Teste",
                null,
                null,
                null,
                1L,
                null,
                null
        );
    }

    public static CreateTaskRequest createEmpryBody(){
        List<Long> participantsIdList = new ArrayList<>();
        participantsIdList.add(1L);
        participantsIdList.add(3L);

        return new CreateTaskRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
