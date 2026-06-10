package br.com.unisinos.gerenciador_tarefas.dto.request.auth;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email (message = ErrorMessages.INVALID_EMAIL)
        @NotNull(message = ErrorMessages.EMPTY_EMAIL)
        String email,
        @NotNull(message = ErrorMessages.EMPTY_PASSWORD)
        String password
) {
}
