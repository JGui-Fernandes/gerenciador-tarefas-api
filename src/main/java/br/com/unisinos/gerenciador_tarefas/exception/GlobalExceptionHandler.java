package br.com.unisinos.gerenciador_tarefas.exception;

import br.com.unisinos.gerenciador_tarefas.dto.response.ErrorMessageResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.FieldErrorResponse;
import br.com.unisinos.gerenciador_tarefas.dto.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleTaskNotFound(
            TaskNotFoundException ex
    ) {

        ErrorMessageResponse response =
                new ErrorMessageResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessageResponse>
    handleInvalidCredentials(
            InvalidCredentialsException ex
    ) {

        ErrorMessageResponse response =
                new ErrorMessageResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage()
                );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse>
    handleValidationError(
            MethodArgumentNotValidException ex
    ) {

        List<FieldErrorResponse> errors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                new FieldErrorResponse(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                        )
                        .toList();

        ValidationErrorResponse response =
                new ValidationErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Erro no envio do formulário",
                        errors
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}