package br.com.unisinos.gerenciador_tarefas.dto.response.auth;

import br.com.unisinos.gerenciador_tarefas.dto.response.user.ListUserResponse;

public record LoginResponse(
        String token,
        ListUserResponse user
) {
}
