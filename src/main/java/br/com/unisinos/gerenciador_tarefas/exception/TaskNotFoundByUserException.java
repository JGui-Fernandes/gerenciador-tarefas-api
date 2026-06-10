package br.com.unisinos.gerenciador_tarefas.exception;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;

public class TaskNotFoundByUserException extends RuntimeException {
    public TaskNotFoundByUserException() {
        super(ErrorMessages.TASK_NOT_FOUND_BY_USER);
    }
}
