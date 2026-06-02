package br.com.unisinos.gerenciador_tarefas.dto.request.user;

import br.com.unisinos.gerenciador_tarefas.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotNull(message = "O campo name é obrigatório")
        String name,
        @Email (message = "Formato incorreto para o campo email")
        @NotNull (message = "O campo email é obrigatório")
        String email,
        @NotNull (message = "O campo password é obrigatório")
        String password,
        LocalDate birthDate,
        String phone,
        UserRole role
) {
}
