package dev.suel.mstransactionapi.infra.web.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import dev.suel.mstransactionapi.application.exception.InvalidOperationException;
import dev.suel.mstransactionapi.application.exception.ResourceNotFoundException;
import dev.suel.mstransactionapi.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        errors)
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String paramName = ex.getParameterName();

        String message = String.format(
                "O parâmetro '%s' é obrigatório.",
                paramName
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(

                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        request.getRequestURI(),
                        null)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> methodArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String paramName = ex.getParameter().getParameterName();
        Object value = ex.getValue();

        String message = String.format(
                "O parâmetro '%s' com valor '%s' é inválido.",
                paramName,
                value
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(

                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        request.getRequestURI(),
                        null)
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleInvalidDomainRulesData(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );

    }

    @ExceptionHandler({
            BadCredentialsException.class,
            AuthorizationDeniedException.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse> handleBadCredentialsException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );
    }

    @ExceptionHandler({TokenExpiredException.class, JWTVerificationException.class})
    public ResponseEntity<ApiResponse> handleJWTVerificationException(Exception ex,
                                                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.PRECONDITION_FAILED.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiResponse> handleInvalidOperationException(InvalidOperationException ex,
                                                                       HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        null)
        );
    }
}
