package br.com.unisinos.gerenciador_tarefas.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("Nenhum usuário encontrado com esse ID");
    }
}
