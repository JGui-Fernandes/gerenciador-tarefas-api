package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.UserDetailResponse;

public interface UserService {

    UserDetailResponse create(CreateUserRequest request);

    UserDetailResponse findById(Long id);

    UserDetailResponse update(
            Long id,
            CreateUserRequest request
    );

    void delete(Long id);

}
