package br.com.unisinos.gerenciador_tarefas.dto.response;

public record ListUserResponse(
        Long id,
        String name,
        String email
) {
}
