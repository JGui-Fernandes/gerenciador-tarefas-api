package br.com.unisinos.gerenciador_tarefas.repository;

import br.com.unisinos.gerenciador_tarefas.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssigneeId(Long assigneeId);
}
