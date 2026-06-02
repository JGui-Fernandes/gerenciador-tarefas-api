package br.com.unisinos.gerenciador_tarefas.repository;

import br.com.unisinos.gerenciador_tarefas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByPhone(String phone);

    Optional<User> findByIdAndIsActiveTrue(Long id);
    List<User> findByIdInAndIsActiveTrue(List<Long> ids);
}
