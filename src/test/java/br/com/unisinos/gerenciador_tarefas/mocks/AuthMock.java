package br.com.unisinos.gerenciador_tarefas.mocks;

import br.com.unisinos.gerenciador_tarefas.dto.response.TokenResponse;

public class AuthMock {

    public static TokenResponse tokenResponse() {

        return new TokenResponse(
                "jwt-token-fake",
                UserMock.listUserResponse()
        );
    }
}
