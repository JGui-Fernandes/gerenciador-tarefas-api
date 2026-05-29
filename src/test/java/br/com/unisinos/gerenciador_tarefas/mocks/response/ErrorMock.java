package br.com.unisinos.gerenciador_tarefas.mocks.response;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.dto.response.ErrorMessageResponse;

public class ErrorMock {

    public static ErrorMessageResponse fillAllMandatoryFields(){

        return new ErrorMessageResponse(
                400,
                ErrorMessages.FILL_ALL_FIELDS
        );
    }

    public static ErrorMessageResponse userUnauthorized() {

        return new ErrorMessageResponse(
                401,
                ErrorMessages.UNLOGGED_USER
        );
    }

    public static ErrorMessageResponse taskNotFoundById() {

        return new ErrorMessageResponse(
                404,
                ErrorMessages.TASK_NOT_FOUND_BY_ID
        );
    }

    public static ErrorMessageResponse taskNotFoundByUser() {

        return new ErrorMessageResponse(
                404,
                ErrorMessages.TASK_NOT_FOUND_BY_USER
        );
    }

    public static ErrorMessageResponse badRequest() {

        return new ErrorMessageResponse(
                400,
                "o campo x é inválido"
        );
    }
}