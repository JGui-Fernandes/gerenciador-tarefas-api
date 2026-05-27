package br.com.unisinos.gerenciador_tarefas.dto.request;

import java.time.LocalDate;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        LocalDate birthDate,
        String phone
) {
}
