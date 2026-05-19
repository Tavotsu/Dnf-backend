package com.usuario.Ms_usuario.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleValidationExceptions() {
        // Arrange (Preparación)
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // Mockeamos los parámetros requeridos por la excepción
        MethodParameter parameter = mock(MethodParameter.class);
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        // Simulamos dos errores de validación de Spring
        FieldError fieldError1 = new FieldError("usuario", "email", "El email es obligatorio");
        FieldError fieldError2 = new FieldError("usuario", "name", "El nombre no puede estar vacío");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // Act (Ejecución)
        Map<String, String> errores = handler.handleValidationExceptions(exception);

        // Assert (Verificación)
        assertEquals(2, errores.size());
        assertEquals("El email es obligatorio", errores.get("email"));
        assertEquals("El nombre no puede estar vacío", errores.get("name"));
    }
}