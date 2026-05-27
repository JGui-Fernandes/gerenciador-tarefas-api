package br.com.unisinos.gerenciador_tarefas.mocks;

public class UserMock {

    public static UserDetailResponse userDetailResponse() {

        return new UserDetailResponse(
                1L,
                "John",
                "john@email.com",
                LocalDate.of(2000, 1, 1),
                "51999999999",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ListUserResponse listUserResponse() {

        return new ListUserResponse(
                1L,
                "John",
                "john@email.com"
        );
    }
}