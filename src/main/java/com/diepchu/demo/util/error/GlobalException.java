package com.diepchu.demo.util.error;

import com.diepchu.demo.domain.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {IdInvalidException.class,
                                BadCredentialsException.class,
                                UsernameNotFoundException.class,})
    public ResponseEntity<RestResponse<Object>> handleBIdInvalidException(IdInvalidException idInvalidException) {
        RestResponse<Object> restResonse = new RestResponse<Object>();
        restResonse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResonse.setMessage("idInvalidException");
        restResonse.setError(idInvalidException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResonse);
    }

    @ExceptionHandler(value =  {
            NoResourceFoundException.class
    })
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage());
        res.setMessage("404 Not Found. URL may not exist...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationEror (MethodArgumentNotValidException methodArgmentNotValidException) {
        BindingResult bindingResult = methodArgmentNotValidException.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        RestResponse<Object> restResonse = new RestResponse<Object>();
        restResonse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResonse.setError(methodArgmentNotValidException.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toUnmodifiableList());
        restResonse.setMessage(errors.size() > 1 ? errors.get(0) : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResonse);
    }

    @ExceptionHandler(value = {StorageException.class})
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(IdInvalidException idInvalidException) {
        RestResponse<Object> restResonse = new RestResponse<Object>();
        restResonse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResonse.setMessage("Exception upload file...");
        restResonse.setError(idInvalidException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResonse);
    }
}
