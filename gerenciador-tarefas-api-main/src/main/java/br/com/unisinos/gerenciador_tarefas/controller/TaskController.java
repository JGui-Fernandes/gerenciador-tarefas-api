package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.dto.request.task.CreateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.task.UpdateTaskRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.ListTaskResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.task.TaskDetailResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ValidationErrorResponse;
import br.com.unisinos.gerenciador_tarefas.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Tasks", description = "Operações relacionadas a tarefas")
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping
    @Operation(
            summary = "Criar uma nova tarefa",
            description = "Cadastra uma tarefa"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarefa cadastrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no envio do formulário",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationErrorResponse.class
                            )
                    )
            )
    })

    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateTaskRequest request
    ) {
        service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar tarefa por ID",
            description = "Retorna os detalhes de uma tarefa específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = TaskDetailResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 404,
                                      "message": "Nenhuma tarefa encontrada com esse ID"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<TaskDetailResponse> findById(
            @Parameter(
                    description = "ID da tarefa",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Buscar tarefas pelo responsável",
            description = "Retorna a lista de tarefas nas quais um usuário é responsável"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de tarefas",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ListTaskResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 404,
                                      "message": "Nenhum usuário encontrado com esse ID"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<List<ListTaskResponse>> findAllAssignedTo(
            @Parameter(
                    description = "ID do usuário",
                    example = "1",
                    required = true
            )
            @RequestParam Long assignedTo
    ) {
        return ResponseEntity.ok(service.findAllAssignedTo(assignedTo));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Editar tarefa",
            description = "Realiza a edição de uma tarefa já cadastrada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa editada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = TaskDetailResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 404,
                                      "message": "Nenhuma tarefa encontrada com esse ID"
                                    }
                                    """

                            )
                    )
            )
    })
    public ResponseEntity<TaskDetailResponse> update(
            @Parameter(
                    description = "ID da tarefa",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody @Valid UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar tarefa",
            description = "Realiza a deleção de uma tarefa já cadastrada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Tarefa deletada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 404,
                                      "message": "Nenhuma tarefa encontrada com esse ID"
                                    }
                                    """

                            )
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "ID da tarefa",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}