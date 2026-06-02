package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.task.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.task.UpdateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.Task;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.exception.TaskNotFoundException;
import br.com.unisinos.gerenciador_tarefas.exception.UserNotFoundException;
import br.com.unisinos.gerenciador_tarefas.repository.TaskRepository;
import br.com.unisinos.gerenciador_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public void create(CreateTaskRequest request) {
        Task task = new Task();
        task.setName(request.name());
        task.setDescription(request.description());
        task.setDeadline(request.deadline());

        task.setStatus(request.status());

        if (request.assigneeId() != null) {
            User assignee = userRepository.findByIdAndIsActiveTrue(request.assigneeId())
                    .orElseThrow(UserNotFoundException::new);
            task.setAssignee(assignee);
        }

        if (request.participantsId() != null && !request.participantsId().isEmpty()) {
            List<User> users = userRepository.findByIdInAndIsActiveTrue(request.participantsId());

            if (users.size() != request.participantsId().size()) {
                throw new UserNotFoundException();
            }

            for (User user : users) {
                if (task.getParticipants().contains(user)) {
                    continue;
                }
                task.getParticipants().add(user);
            }
        }
    }


    public TaskDetailResponse findById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException()
                );

        return new TaskDetailResponse(task);
    }

    public List<ListTaskResponse> findAllAssignedTo(Long assignedTo) {

        User u = userRepository.findById(assignedTo)
                .orElseThrow(()->
                        new UserNotFoundException()
                );
        return u.getAssignedTasks()
                    .stream()
                    .map(ListTaskResponse::new)
                    .toList();
    }

    public TaskDetailResponse update(Long id, UpdateTaskRequest request){

        Task task = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException()
        );
        if(request.description() != null){
            task.setDescription(request.description());
        }
        if(request.status() != null){
            task.setStatus(request.status());
        }
        if(request.assigneeId() != null){
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() ->
                            new UserNotFoundException()
                    );
            task.setAssignee(assignee);
        }
        if(request.deadline() != null){
            task.setDeadline(request.deadline());
        }
        if(request.name() != null){
            task.setName(request.name());
        }

        taskRepository.save(task);

        return new TaskDetailResponse(task);
    }

    public void delete(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(()->
                    new TaskNotFoundException()
                );

        taskRepository.delete(task);
    }
}