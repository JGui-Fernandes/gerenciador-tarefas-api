package br.com.unisinos.gerenciador_tarefas.dto.response.user;

import br.com.unisinos.gerenciador_tarefas.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDetailResponse(
        Long id,
        String name,
        String email,
        LocalDate birthDate,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public UserDetailResponse(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getBirthDate(), u.getPhone(), u.getCreatedAt(), u.getUpdatedAt());
    }
}
