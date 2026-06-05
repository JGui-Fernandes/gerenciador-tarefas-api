package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.user.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.UpdateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.UserDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.BadRequestException;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.exception.UserNotFoundException;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(CreateUserRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new BadRequestException("Email já cadastrado");
        }
        if (userRepository.findByPhone(request.phone()) != null) {
            throw new BadRequestException("Telefone já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setBirthDate(request.birthDate());
        user.setPassword(request.password());
        user.setPhone(request.phone()); // TODO criptografar senha
        user.setRole(request.role());

        userRepository.save(user);
    }

    public UserDetailResponse findById(Long id){
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new TaskNotFoundException()
                );
        return new UserDetailResponse(user);

    }

    public UserDetailResponse update(Long id, UpdateUserRequest request){
        User u = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(()->
                        new UserNotFoundException()
                );
        if(request.phone() != null && !request.phone().isBlank()){
            if (userRepository.findByPhone(request.phone()) != null) {
                throw new BadRequestException("Telefone já cadastrado");
            }
            u.setEmail(request.email());
        }
        if(request.email() != null && !request.email().isBlank()){
            if (userRepository.findByEmail(request.email()) != null) {
                throw new BadRequestException("Email já cadastrado");
            }
            u.setEmail(request.email());
        }
        if(request.name() != null && !request.name().isBlank()){
            u.setName(request.name());
        }
        if(request.role() != null){
            u.setRole(request.role());
        }
        if(request.birthDate() != null){
            u.setBirthDate(request.birthDate());
        }
        if(request.password()!= null && !request.password().isBlank()){
            u.setPassword(request.password());
        }

        userRepository.save(u);
        return new UserDetailResponse(u);

    }

    public void delete(Long id){
        User u = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(()->
                        new UserNotFoundException()
                    );

        u.setActive(false);

        userRepository.save(u);
    }

}
