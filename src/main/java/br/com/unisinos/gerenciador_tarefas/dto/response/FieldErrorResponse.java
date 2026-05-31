package br.com.unisinos.gerenciador_tarefas.dto.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
