package br.com.unisinos.gerenciador_tarefas.mocks.response;

import br.com.unisinos.gerenciador_tarefas.dto.response.user.ListUserResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.UserDetailResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public static List<ListUserResponse> listUserResponseList() {
        return List.of(
                new ListUserResponse(1L, "John", "john@email.com"),
                new ListUserResponse(2L, "Jane", "jane@email.com"),
                new ListUserResponse(3L, "Bob", "bob@email.com")
        );
    }
}
