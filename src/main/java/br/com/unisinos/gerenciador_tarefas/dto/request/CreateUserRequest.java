package br.com.unisinos.gerenciador_tarefas.dto.request;

import java.util.Date;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        Date birthDate,
        String phone
) {
}
