package br.com.unisinos.gerenciador_tarefas.dto.response;

public record ErrorMessageResponse(
        int statusCode,
        String message
) {
}
