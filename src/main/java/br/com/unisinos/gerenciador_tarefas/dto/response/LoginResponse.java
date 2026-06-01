package br.com.unisinos.gerenciador_tarefas.dto.response;

public record LoginResponse(
        String token,
        ListUserResponse user
) {
}
