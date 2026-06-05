package br.com.unisinos.gerenciador_tarefas.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email (message = "Formato incorreto para o campo email")
        @NotNull(message = "O campo email é obrigatório")
        String email,
        @NotNull(message = "O campo password é obrigatório")
        String password
) {
}
