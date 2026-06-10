package br.com.unisinos.gerenciador_tarefas.dto.request.user;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotNull(message = ErrorMessages.EMPTY_EMAIL)
        String name,
        @Email (message = ErrorMessages.INVALID_EMAIL)
        @NotNull (message = ErrorMessages.EMPTY_EMAIL)
        String email,
        @NotNull (message = ErrorMessages.EMPTY_PASSWORD)
        String password,
        LocalDate birthDate,
        String phone,
        UserRole role
) {
}
