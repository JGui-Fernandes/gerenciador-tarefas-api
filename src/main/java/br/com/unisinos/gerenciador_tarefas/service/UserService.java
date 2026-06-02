package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.UserDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetailResponse create(CreateUserRequest request){
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBirthDate(request.birthDate());
        user.setPhone(request.phone());
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        return new UserDetailResponse(savedUser);
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
