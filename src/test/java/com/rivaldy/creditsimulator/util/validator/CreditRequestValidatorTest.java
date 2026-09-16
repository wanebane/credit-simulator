package com.rivaldy.creditsimulator.util.validator;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class CreditRequestValidatorTest {

    private CreditRequestValidator validator;
    private CreditSimulationRequest validRequest;

    @BeforeEach
    void setUp(){
        validator = new CreditRequestValidator();

        validRequest = new CreditSimulationRequest();
        validRequest.setVehicleType("Mobil");
        validRequest.setVehicleCondition("Baru");
        validRequest.setVehicleYear(Year.now().getValue());
        validRequest.setTotalLoanAmount(new BigDecimal("100000000"));
        validRequest.setDownPayment(new BigDecimal("35000000"));
        validRequest.setLoanTenure(3);
    }

    @Test
    @DisplayName("Must success validation if all request input is valid")
    void validate_ValidRequest_NoExceptionThrown() {
        assertDoesNotThrow(() -> validator.validate(validRequest));
    }

    @Test
    @DisplayName("Failed to validate if vehicle type is not valid")
    void validate_InvalidVehicleType_ThrowsException() {
        validRequest.setVehicleType("Pesawat");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertEquals("Jenis kendaraan harus 'Mobil' atau 'Motor'", ex.getMessage());
    }

    @Test
    @DisplayName("Failed to validate if vehicle condition is not valid")
    void validate_InvalidVehicleCondition_ThrowsException() {
        validRequest.setVehicleCondition("Setengah Pakai");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertEquals("Kondisi kendaraan harus 'Baru' atau 'Bekas'", ex.getMessage());
    }

    @Test
    @DisplayName("Failed to validate if total loan amount more than 10 Billion")
    void validate_LoanExceedsMaximum_ThrowsException() {
        validRequest.setTotalLoanAmount(new BigDecimal("10000000001"));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertEquals("Jumlah pinjaman total tidak boleh lebih dari Rp 10.000.000.000", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 7, -1})
    @DisplayName("Failed to validate if loan tenure not in range 1-6 years")
    void validate_InvalidTenure_ThrowsException(int invalidTenure) {
        validRequest.setLoanTenure(invalidTenure);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertEquals("Tenor pinjaman harus antara 1 sampai 6 tahun", ex.getMessage());
    }

    @Test
    @DisplayName("Failed to validate iif vehicle condition NEW with year more than 1 year")
    void validate_InvalidNewVehicleYear_ThrowsException() {
        int currentYear = Year.now().getValue();
        int invalidYear = currentYear - 2;

        validRequest.setVehicleCondition("Baru");
        validRequest.setVehicleYear(invalidYear);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertTrue(ex.getMessage().contains("Kendaraan dengan kondisi BARU tidak boleh diinput tahun kurang dari"));
    }

    @Test
    @DisplayName("Failed to validate if Down Payment less than limit minimum percentage")
    void validate_InsufficientDownPayment_ThrowsException() {
        validRequest.setDownPayment(new BigDecimal("20000000"));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(validRequest)
        );
        assertTrue(ex.getMessage().contains("Jumlah DP untuk kendaraan BARU minimal 35%"));
    }
}