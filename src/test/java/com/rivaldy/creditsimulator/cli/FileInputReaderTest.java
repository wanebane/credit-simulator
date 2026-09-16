package com.rivaldy.creditsimulator.cli;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileInputReaderTest {

    private FileInputReader fileInputReader;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        fileInputReader = new FileInputReader(objectMapper);
    }

    @Test
    @DisplayName("Should successfully read and map valid txt properties file into CreditSimulationRequest")
    void readRequest_ValidTxtFile_ReturnsMappedRequest(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("sample-input.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("vehicleType=Mobil\n");
            writer.write("vehicleCondition=Baru\n");
            writer.write("vehicleYear=2025\n");
            writer.write("totalLoanAmount=100000000\n");
            writer.write("loanTenure=3\n");
            writer.write("downPayment=35000000\n");
        }

        CreditSimulationRequest request = fileInputReader.readRequest(tempFile.getAbsolutePath());

        assertNotNull(request);
        assertEquals("Mobil", request.getVehicleType());
        assertEquals("Baru", request.getVehicleCondition());
        assertEquals(2025, request.getVehicleYear());
        assertEquals(0, new BigDecimal("100000000").compareTo(request.getTotalLoanAmount()));
        assertEquals(3, request.getLoanTenure());
        assertEquals(0, new BigDecimal("35000000").compareTo(request.getDownPayment()));
    }

    @Test
    @DisplayName("Should throw ValidationException when file path is null or empty")
    void readRequest_NullOrEmptyFilePath_ThrowsValidationException() {
        ValidationException exNull = assertThrows(
                ValidationException.class,
                () -> fileInputReader.readRequest(null)
        );
        assertEquals("Path file input tidak boleh kosong", exNull.getMessage());

        ValidationException exBlank = assertThrows(
                ValidationException.class,
                () -> fileInputReader.readRequest("   ")
        );
        assertEquals("Path file input tidak boleh kosong", exBlank.getMessage());
    }

    @Test
    @DisplayName("Should throw ValidationException when file does not exist")
    void readRequest_FileNotFound_ThrowsValidationException() {
        String invalidPath = "input-file/non-existent.txt";

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> fileInputReader.readRequest(invalidPath)
        );
        assertTrue(exception.getMessage().contains("File tidak ditemukan pada path :"));
    }
}