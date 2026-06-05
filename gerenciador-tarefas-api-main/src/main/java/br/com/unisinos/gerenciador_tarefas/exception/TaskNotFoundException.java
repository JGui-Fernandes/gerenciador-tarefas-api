package br.com.unisinos.gerenciador_tarefas.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException() {
        super("Nenhuma tarefa encontrada com esse ID");
    }
}
