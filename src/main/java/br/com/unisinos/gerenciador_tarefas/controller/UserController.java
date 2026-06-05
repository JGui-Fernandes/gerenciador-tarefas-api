package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.dto.request.user.CreateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.request.user.UpdateUserRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ValidationErrorResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.user.UserDetailResponse;
import br.com.unisinos.gerenciador_tarefas.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operações relacionadas a usuários")
public class UserController {

    @Autowired
    private UserService service;

    @Transactional
    @PostMapping
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cadastra um novo usuário no sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no envio do formulário ou dados já cadastrados",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationErrorResponse.class
                            )
                    )
            )
    })
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateUserRequest request
    ) {
        service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os detalhes de um usuário específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserDetailResponse.class
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
    public ResponseEntity<UserDetailResponse> findById(
            @Parameter(
                    description = "ID do usuário",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Transactional
    @PutMapping("/{id}")
    @Operation(
            summary = "Editar usuário",
            description = "Realiza a edição de um usuário já cadastrado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário editado com sucesso",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserDetailResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou já cadastrados",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 400,
                                      "message": "Email já cadastrado"
                                    }
                                    """
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
    public ResponseEntity<UserDetailResponse> update(
            @Parameter(
                    description = "ID do usuário",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar usuário",
            description = "Realiza a desativação de um usuário cadastrado (soft delete)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário deletado com sucesso"
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
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "ID do usuário",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping
    @Operation(
            summary = "Editar usuário autenticado",
            description = "Realiza a edição do próprio usuário autenticado, sem necessidade de informar o ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário editado com sucesso",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserDetailResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou já cadastrados",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "statusCode": 400,
                                      "message": "Email já cadastrado"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<UserDetailResponse> update(
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return ResponseEntity.ok(service.update(null, request));
    }
}