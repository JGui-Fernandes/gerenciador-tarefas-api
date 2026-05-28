package br.com.unisinos.gerenciador_tarefas.exception;

import br.com.unisinos.gerenciador_tarefas.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse>
    handleTaskNotFound(
            TaskNotFoundException ex
    ) {

        ErrorMessageResponse response =
                new ErrorMessageResponse(
                        404,
                        ex.getMessage()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}