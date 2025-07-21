package com.neoage.exception;

import com.neoage.constant.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestControllerAdvice
public class ProductManagementExceptionHandler {

    @ExceptionHandler(ProductManagementException.class)
    public Mono<ProblemDetail> handleRuntime(ProductManagementException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(ex.getErrorCode().getHttpStatus(), ex.getMessage());
        problem.setTitle(ex.getErrorCode().name());
        problem.setType(URI.create(""));
        return Mono.just(problem);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ProblemDetail> handleValidation(ServerWebInputException ex) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getReason());
        problem.setTitle(ErrorCode.INVALID_INPUT.name());
        problem.setType(URI.create(""));
        return Mono.just(problem);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public Mono<ProblemDetail> handleOtherErrors(ErrorResponseException ex) {
        return Mono.just(ex.getBody());
    }
}
