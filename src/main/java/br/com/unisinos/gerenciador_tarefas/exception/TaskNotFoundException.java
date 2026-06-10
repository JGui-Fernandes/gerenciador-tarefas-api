package br.com.unisinos.gerenciador_tarefas.exception;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException() {
        super(ErrorMessages.TASK_NOT_FOUND_BY_ID);
    }
}
