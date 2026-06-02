package br.com.unisinos.gerenciador_tarefas.dto.request.user;

import br.com.unisinos.gerenciador_tarefas.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateUserRequest(
        String name,
        @Email(message = "Formato incorreto para o campo email")
        String email,
        String password,
        LocalDate birthDate,
        String phone,
        UserRole role
) {
}
