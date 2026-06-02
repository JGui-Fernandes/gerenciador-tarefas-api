package br.com.unisinos.gerenciador_tarefas.config;

import br.com.unisinos.gerenciador_tarefas.entities.User;

public interface JwtUtil {

    String generateToken(User user);

    boolean validateToken(String token);

    String extractUsername(String token);
}
