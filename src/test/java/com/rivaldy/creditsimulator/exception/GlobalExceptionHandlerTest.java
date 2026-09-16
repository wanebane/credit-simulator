package com.rivaldy.creditsimulator.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Must be return HTTP 400 when handle ValidationException")
    void handleValidationException_ReturnsBadRequest() {
        ValidationException exception = new ValidationException("Batas pinjaman melebihi batas");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> responseEntity =
                exceptionHandler.handleValidationException(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        GlobalExceptionHandler.ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("Batas pinjaman melebihi batas", body.message());
        assertNotNull(body.timestamp());
    }

    @Test
    @DisplayName("Must be return HTTP 400 and error map when handle MethodArgumentNotValidException")
    void handleMethodArgumentNotValid_ReturnsFieldErrorsMap() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("request", "vehicleType", "Jenis kendaraan wajib diisi");
        FieldError fieldError2 = new FieldError("request", "loanTenure", "Tenor minimal 1 tahun");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> responseEntity =
                exceptionHandler.handleMethodArgumentNotValid(ex);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        GlobalExceptionHandler.ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Validation Error", body.error());

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.message();
        assertEquals(2, errors.size());
        assertEquals("Jenis kendaraan wajib diisi", errors.get("vehicleType"));
        assertEquals("Tenor minimal 1 tahun", errors.get("loanTenure"));
    }

    @Test
    @DisplayName("Must be return HTTP 500 when handle Generic Exception")
    void handleGenericException_ReturnsInternalServerError() {
        Exception exception = new RuntimeException("Terjadi kesalahan sistem internal");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> responseEntity =
                exceptionHandler.handleGenericException(exception);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        GlobalExceptionHandler.ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("Internal Server Error", body.error());
        assertEquals("Terjadi kesalahan sistem internal", body.message());
    }
}