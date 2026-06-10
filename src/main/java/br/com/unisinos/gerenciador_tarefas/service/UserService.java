package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.constants.ErrorMessages;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.UpdateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.ListUserResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.UserDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.BadRequestException;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.exception.UserNotFoundException;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void create(CreateUserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException(ErrorMessages.REUSED_EMAIL);
        }

        if (request.phone() != null && userRepository.findByPhone(request.phone()).isPresent()) {
            throw new BadRequestException(ErrorMessages.REUSED_PHONE);
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setBirthDate(request.birthDate());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.setRole(request.role());
        user.setActive(true);
  
        userRepository.save(user);
    }

    public UserDetailResponse findById(Long id){
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(UserNotFoundException::new
                );
        return new UserDetailResponse(user);

    }

    public UserDetailResponse update(Long id, UpdateUserRequest request){
        User u;
        if(id != null){
            u = userRepository.findByIdAndIsActiveTrue(id)
                    .orElseThrow(UserNotFoundException::new
                    );
        }
        else{
            u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        if(request.phone() != null && !request.phone().isBlank()){
            if (userRepository.findByPhoneAndIdNot(request.phone(), id).isPresent()) {
                throw new BadRequestException(ErrorMessages.REUSED_PHONE);
            }
            u.setPhone(request.phone());
        }
        if(request.email() != null && !request.email().isBlank()){
            if (userRepository.findByEmailAndIdNot(request.email(), id).isPresent()) {
                throw new BadRequestException(ErrorMessages.REUSED_EMAIL);
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
            u.setPassword(passwordEncoder.encode(request.password()));
        }

        userRepository.save(u);
        return new UserDetailResponse(u);

    }

    public void delete(Long id){
        User u = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(UserNotFoundException::new
                    );

        u.setActive(false);

        userRepository.save(u);
    }

    public List<ListUserResponse> listActiveUsers(){
        List<User> list = userRepository.findByIsActiveTrue();

        return list.stream()
                .map(ListUserResponse::new)
                .toList();
    }

    public UserDetailResponse detailLoggedUser(){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new UserDetailResponse(u);
    }

    public void deleteLoggedUser(){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        u.setActive(false);

        userRepository.save(u);
    }

}
