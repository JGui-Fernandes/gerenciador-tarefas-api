package br.com.unisinos.gerenciador_tarefas.dto.response.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
