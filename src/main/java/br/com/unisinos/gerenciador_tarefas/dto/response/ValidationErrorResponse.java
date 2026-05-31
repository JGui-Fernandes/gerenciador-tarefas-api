package br.com.unisinos.gerenciador_tarefas.dto.response;

import java.util.List;

public record ValidationErrorResponse(
        Integer statusCode,
        String message,
        List<FieldErrorResponse> errors
) {
}
