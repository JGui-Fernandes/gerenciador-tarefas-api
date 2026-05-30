package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.UserDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetailResponse create(CreateUserRequest request){
        return null;
    }

    public UserDetailResponse findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException()
                );
        return new UserDetailResponse(user);

    }

    public UserDetailResponse update(Long id, CreateUserRequest request){
        return null;

    }

    public void delete(Long id){

    }

}
