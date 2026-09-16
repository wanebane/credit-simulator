package com.rivaldy.creditsimulator.util.display;

import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.dto.response.YearlyInstallmentDetailResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditSimulatorCliDisplayTest {

    private CreditSimulatorCliDisplay cliDisplay;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        cliDisplay = new CreditSimulatorCliDisplay();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should successfully render credit simulation summary and yearly breakdown table")
    void displayResult_ValidResponse_PrintsExpectedOutput() {
        YearlyInstallmentDetailResponse year1 = YearlyInstallmentDetailResponse.builder()
                .year(1)
                .principalAmount(new BigDecimal("65000000.00"))
                .interestRate(8.0)
                .totalLoanAmount(new BigDecimal("70200000.00"))
                .monthlyInstallment(new BigDecimal("1950000.00"))
                .yearlyInstallment(new BigDecimal("23400000.00"))
                .build();

        YearlyInstallmentDetailResponse year2 = YearlyInstallmentDetailResponse.builder()
                .year(2)
                .principalAmount(new BigDecimal("46800000.00"))
                .interestRate(8.1)
                .totalLoanAmount(new BigDecimal("50590800.00"))
                .monthlyInstallment(new BigDecimal("2107950.00"))
                .yearlyInstallment(new BigDecimal("25295400.00"))
                .build();

        CreditSimulationResponse response = CreditSimulationResponse.builder()
                .vehicleType("Mobil")
                .vehicleCondition("Baru")
                .vehicleYear(2025)
                .totalLoanAmount(new BigDecimal("100000000.00"))
                .downPayment(new BigDecimal("35000000.00"))
                .principalAmount(new BigDecimal("65000000.00"))
                .loanTenure(2)
                .yearlyInstallments(List.of(year1, year2))
                .averageMonthlyInstallment(new BigDecimal("2028975.00"))
                .build();

        assertDoesNotThrow(() -> cliDisplay.displayResult(response));

        String printedContent = outputStreamCaptor.toString();

        assertTrue(printedContent.contains("HASIL SIMULASI KREDIT"));
        assertTrue(printedContent.contains("RINCIAN ANGSURAN PER TAHUN"));

        assertTrue(printedContent.contains("Jenis Kendaraan           : Mobil"));
        assertTrue(printedContent.contains("Kondisi Kendaraan         : Baru"));
        assertTrue(printedContent.contains("Tahun Kendaraan           : 2025"));
        assertTrue(printedContent.contains("Tenor                     : 2 Tahun"));

        assertTrue(printedContent.contains("Tahun 1"));
        assertTrue(printedContent.contains("Tahun 2"));
        assertTrue(printedContent.contains("Pokok Pinjaman"));
        assertTrue(printedContent.contains("Rate"));
        assertTrue(printedContent.contains("Total Pinjaman"));
        assertTrue(printedContent.contains("Angsuran / bulan"));
        assertTrue(printedContent.contains("Angsuran / tahun"));
    }

    @Test
    @DisplayName("Should handle null monetary fields gracefully and print default currency format")
    void displayResult_WithNullFields_HandlesGracefully() {
        YearlyInstallmentDetailResponse yearWithNulls = YearlyInstallmentDetailResponse.builder()
                .year(1)
                .principalAmount(null)
                .interestRate(8.0)
                .totalLoanAmount(null)
                .monthlyInstallment(null)
                .yearlyInstallment(null)
                .build();

        CreditSimulationResponse response = CreditSimulationResponse.builder()
                .vehicleType("Motor")
                .vehicleCondition("Bekas")
                .vehicleYear(2023)
                .totalLoanAmount(null)
                .downPayment(null)
                .principalAmount(null)
                .loanTenure(1)
                .yearlyInstallments(List.of(yearWithNulls))
                .averageMonthlyInstallment(null)
                .build();

        assertDoesNotThrow(() -> cliDisplay.displayResult(response));

        String printedContent = outputStreamCaptor.toString();
        assertTrue(printedContent.contains("Rp. 0,00"));
    }
}