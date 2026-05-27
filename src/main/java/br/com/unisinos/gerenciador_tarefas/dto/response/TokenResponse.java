package br.com.unisinos.gerenciador_tarefas.dto.response;

public record TokenResponse(
        String token,
        ListUserResponse user
) {
}
