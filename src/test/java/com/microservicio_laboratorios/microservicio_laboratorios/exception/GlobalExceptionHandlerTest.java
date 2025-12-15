package com.microservicio_laboratorios.microservicio_laboratorios.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_RetornarNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("Recurso no encontrado", body.getMensaje());
        assertEquals("Recurso no encontrado", body.getDetalles());
    }

    @Test
    void handleBadRequest_RetornarBadRequest() {
        BadRequestException ex = new BadRequestException("Solicitud invalida");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleBadRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("Solicitud invalida", body.getMensaje());
        assertEquals("Solicitud inválida", body.getDetalles());
    }

    @Test
    void handleValidationExceptions_RetornarBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "defaultMessage");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("Validación fallida", body.getMensaje());
    }

    @Test
    void handleNoResourceFound_RetornarNotFound() {
        org.springframework.web.servlet.resource.NoResourceFoundException ex = new org.springframework.web.servlet.resource.NoResourceFoundException(
                org.springframework.http.HttpMethod.GET, "/ruta-inexistente");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleNoResourceFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("Ruta no encontrada", body.getMensaje());
    }

    @Test
    void handleGlobal_RetornarInternalServerError() {
        Exception ex = new Exception("Error interno");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleGlobal(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("Error interno", body.getMensaje());
        assertEquals("Error interno del servidor", body.getDetalles());
    }
}
