package br.com.unisinos.gerenciador_tarefas.service;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.UpdateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.entities.Task;
import br.com.unisinos.gerenciador_tarefas.entities.User;
import br.com.unisinos.gerenciador_tarefas.enums.TaskStatus;
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

    public void create(CreateTaskRequest request){
        Task task = new Task();
        if(request.description() != null){
            task.setDescription(request.description());
        }
        if(request.status() != null){
            task.setStatus(request.status());
        } else{
            task.setStatus(TaskStatus.BACKLOG);
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
        if(request.participantsId() != null && !request.participantsId().isEmpty()){
            User user;
            for(Long id : request.participantsId()){
                user = userRepository.findById(id)
                    .orElseThrow(() ->
                            new UserNotFoundException()
                    );
                task.getParticipants().add(user);
            }
        }

        task.setName(request.name());
        task.setCreator(userRepository.getReferenceById(request.creatorId()));

        taskRepository.save(task);
    }

    public TaskDetailResponse findById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException()
                );

        return new TaskDetailResponse(task);
    }

    public List<ListTaskResponse> findAllAssignedTo(Long assignedTo) {

        return taskRepository.findByAssigneeId(assignedTo)
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

    }
}