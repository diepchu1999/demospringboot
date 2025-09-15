package com.diepchu.demo.util.error;

import com.diepchu.demo.domain.RestResonse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResonse<Object>> handleBIdInvalidException(IdInvalidException idInvalidException) {
        RestResonse<Object> restResonse = new RestResonse<Object>();
        restResonse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResonse.setMessage("idInvalidException");
        restResonse.setError(idInvalidException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResonse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResonse<Object>> validationEror (MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        RestResonse<Object> restResonse = new RestResonse<Object>();
        restResonse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResonse.setError(methodArgumentNotValidException.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toUnmodifiableList());
        restResonse.setMessage(errors.size() > 1 ? errors.get(0) : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResonse);
    }
}
