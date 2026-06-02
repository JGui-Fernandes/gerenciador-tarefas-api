package br.com.unisinos.gerenciador_tarefas.dto.response.error;

public record ErrorMessageResponse(
        int statusCode,
        String message
) {
}
