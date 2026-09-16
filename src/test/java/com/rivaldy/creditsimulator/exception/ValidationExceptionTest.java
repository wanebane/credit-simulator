package com.rivaldy.creditsimulator.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    @DisplayName("Must success to return valid exception message")
    void validationException_MessageIsRetained() {
        String errorMessage = "Jenis kendaraan harus 'Mobil' atau 'Motor'";

        ValidationException exception = new ValidationException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}