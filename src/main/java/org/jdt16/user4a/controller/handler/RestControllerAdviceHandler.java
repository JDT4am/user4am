package org.jdt16.user4a.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.jdt16.user4a.dto.response.RestApiResponse;
import org.jdt16.user4a.dto.response.RestApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviceHandler {

    private static final Map<String, Class<?>> BEAN_TO_DTO_CLASS_MAP = Map.of(
            "userRequest", org.jdt16.user4a.dto.request.UserRequest.class
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Serializable> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        this::getJsonPropertyName,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        RestApiResponseError responseError = RestApiResponseError.builder()
                .restApiResponseRequestError(errors)
                .build();

        RestApiResponse<Object> response = RestApiResponse.builder()
                .restApiResponseCode(HttpStatus.BAD_REQUEST.value())
                .restApiResponseResults(null)
                .restApiResponseMessage("Validation failed")
                .restApiResponseError(responseError)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getJsonPropertyName(FieldError fieldError) {
        Class<?> dtoClass = BEAN_TO_DTO_CLASS_MAP.get(fieldError.getObjectName());
        if (dtoClass == null) {
            log.warn("No DTO class mapping found for bean name '{}', using field name as fallback", fieldError.getObjectName());
            return fieldError.getField();
        }
        try {
            var declaredField = dtoClass.getDeclaredField(fieldError.getField());
            var annotation = declaredField.getAnnotation(JsonProperty.class);
            if (annotation != null && !annotation.value().isEmpty()) {
                return annotation.value();
            }
        } catch (NoSuchFieldException e) {
            log.warn("Field '{}' not found in class {}. Using original field name.", fieldError.getField(), dtoClass.getName());
        }
        return fieldError.getField(); // fallback to original field name if JsonProperty not found
    }
}
