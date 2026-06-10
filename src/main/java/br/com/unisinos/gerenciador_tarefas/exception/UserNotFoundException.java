package br.com.unisinos.gerenciador_tarefas.exception;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND_BY_ID);
    }
}
