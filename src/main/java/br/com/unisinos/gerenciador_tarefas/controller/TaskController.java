package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.dto.request.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.UpdateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.service.TaskService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateTaskRequest request
    ) {
        service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ListTaskResponse>> findAllAssignedTo(
            @RequestParam Long assignedTo
    ) {
        return ResponseEntity.ok(service.findAllAssignedTo(assignedTo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDetailResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}