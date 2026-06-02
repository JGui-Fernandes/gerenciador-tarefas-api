package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.LoginRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);

    void logout(String token);
}
