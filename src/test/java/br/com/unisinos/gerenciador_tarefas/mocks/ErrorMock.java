package br.com.unisinos.gerenciador_tarefas.mocks;

import br.com.unisinos.gerenciador_tarefas.dto.response.ErrorMessageResponse;

public class ErrorMock {

    public static ErrorMessageResponse unauthorized() {

        return new ErrorMessageResponse(
                401,
                "usuário não autenticado"
        );
    }

    public static ErrorMessageResponse notFound() {

        return new ErrorMessageResponse(
                404,
                "task não encontrada"
        );
    }

    public static ErrorMessageResponse badRequest() {

        return new ErrorMessageResponse(
                400,
                "o campo x é inválido"
        );
    }
}