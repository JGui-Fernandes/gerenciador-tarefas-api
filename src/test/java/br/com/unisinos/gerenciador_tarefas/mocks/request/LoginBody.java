package br.com.unisinos.gerenciador_tarefas.mocks.request;

import br.com.unisinos.gerenciador_tarefas.dto.request.auth.LoginRequest;

public class LoginBody {

    public static LoginRequest loginValidBody() {
            return new LoginRequest(
                    "john@email.com",
                    "senha123"
            );
        }

        public static LoginRequest loginWithInvalidEmail() {
            return new LoginRequest(
                    "email-invalido",
                    "senha123"
            );
        }

        public static LoginRequest loginWithoutEmail() {
            return new LoginRequest(
                    null,
                    "senha123"
            );
        }

        public static LoginRequest loginWithoutPassword() {
            return new LoginRequest(
                    "john@email.com",
                    null
            );
        }

        public static LoginRequest loginEmptyBody() {
            return new LoginRequest(
                    null,
                    null
            );
        }
    }

