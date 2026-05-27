package br.com.unisinos.gerenciador_tarefas.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
