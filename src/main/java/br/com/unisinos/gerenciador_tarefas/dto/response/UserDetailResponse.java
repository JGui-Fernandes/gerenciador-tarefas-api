package br.com.unisinos.gerenciador_tarefas.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

public record UserDetailResponse(
        Long id,
        String name,
        String email,
        Date birthDate,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
