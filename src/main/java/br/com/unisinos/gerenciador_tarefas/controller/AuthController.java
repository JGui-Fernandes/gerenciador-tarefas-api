package br.com.unisinos.gerenciador_tarefas.controller;

import br.com.unisinos.gerenciador_tarefas.dto.request.auth.LoginRequest;
import br.com.unisinos.gerenciador_tarefas.dto.response.auth.LoginResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.error.ValidationErrorResponse;
import br.com.unisinos.gerenciador_tarefas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Operações de autenticação")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Fazer login",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            schema = @Schema(
                                    implementation = LoginResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação do payload",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationErrorResponse.class
                            )
                    )
            )
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Fazer logout",
            description = "Invalida o token JWT atual"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Logout realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cabeçalho Authorization inválido",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorMessageResponse.class
                            )
                    )
            )
    })
    public ResponseEntity<Void> logout(
            @Parameter(
                    name = HttpHeaders.AUTHORIZATION,
                    description = "Token JWT no formato Bearer {token}",
                    required = true,
                    in = ParameterIn.HEADER
            )
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String token = extractToken(authorizationHeader);
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Cabeçalho Authorization inválido");
        }
        return authorizationHeader.substring(7);
    }
}
