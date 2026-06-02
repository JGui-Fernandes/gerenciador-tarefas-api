package br.com.unisinos.gerenciador_tarefas.mocks.response;

import br.com.unisinos.gerenciador_tarefas.dto.response.auth.LoginResponse;

public class AuthMock {

    public static LoginResponse tokenResponse() {

        return new LoginResponse(
                "jwt-token-fake",
                UserMock.listUserResponse()
        );
    }
}
