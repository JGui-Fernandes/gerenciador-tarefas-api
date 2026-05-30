package br.com.unisinos.gerenciador_tarefas.repository;

import br.com.unisinos.gerenciador_tarefas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
