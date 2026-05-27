package br.com.unisinos.gerenciador_tarefas.dto.response;

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
}
