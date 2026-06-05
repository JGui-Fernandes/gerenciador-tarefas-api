package br.com.unisinos.gerenciador_tarefas.dto.response.user;

import br.com.unisinos.gerenciador_tarefas.entities.User;

public record ListUserResponse(
        Long id,
        String name,
        String email
) {
    public ListUserResponse(User u){
        this(u.getId(), u.getName(), u.getEmail());
    }
}
