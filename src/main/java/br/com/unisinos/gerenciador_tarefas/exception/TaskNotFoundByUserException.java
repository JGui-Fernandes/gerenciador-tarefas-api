package br.com.unisinos.gerenciador_tarefas.exception;

public class TaskNotFoundByUserException extends RuntimeException {
    public TaskNotFoundByUserException() {
        super("Nenhuma task encontrada para este usuário");
    }
}
