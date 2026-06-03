package br.com.unisinos.gerenciador_tarefas.mocks.request;

import br.com.unisinos.gerenciador_tarefas.dto.request.user.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.enums.UserRole;

import java.time.LocalDate;

public class UserBody {

    public static CreateUserRequest createUserFullBody() {
        return new CreateUserRequest(
                "John",
                "john@email.com",
                "senha123",
                LocalDate.of(2000, 1, 1),
                "51999999999",
                UserRole.USER
        );
    }

    public static CreateUserRequest createUserMandatoryBody() {
        return new CreateUserRequest(
                "John",
                "john@email.com",
                "senha123",
                null,
                null,
                null
        );
    }

    public static CreateUserRequest createEmptyBody() {
        return new CreateUserRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static CreateUserRequest createUserWithInvalidEmail() {
        return new CreateUserRequest(
                "John",
                "email-invalido",   // sem @
                "senha123",
                LocalDate.of(2000, 1, 1),
                "51999999999",
                UserRole.USER
        );
    }
}

//    public static CreateUserRequest createUserWithInvalidPhone() {
//        return new CreateUserRequest(
//                "John",
//                "john@email.com",
//                "senha123",
//                LocalDate.of(2000, 1, 1),
//                "abc",              // não é número
//                UserRole.USER
//        );
//    }

//    public static CreateUserRequest createUserWithFutureBirthDate() {
//        return new CreateUserRequest(
//                "John",
//                "john@email.com",
//                "senha123",
//                LocalDate.now().plusYears(1), // data futura
//                "51999999999",
//                UserRole.USER
//        );
//    }