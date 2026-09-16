package com.rivaldy.creditsimulator.controller;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.exception.ValidationException;
import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.service.ExternalWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditSimulatorController.class)
class CreditSimulatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreditCalculatorService creditCalculatorService;

    @MockitoBean
    private ExternalWebService externalWebService;

    private CreditSimulationRequest validRequest;
    private CreditSimulationResponse mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = CreditSimulationRequest.builder()
                .vehicleType("Mobil")
                .vehicleCondition("Baru")
                .vehicleYear(2025)
                .totalLoanAmount(new BigDecimal("100000000"))
                .loanTenure(3)
                .downPayment(new BigDecimal("35000000"))
                .build();

        mockResponse = CreditSimulationResponse.builder()
                .vehicleType("Mobil")
                .vehicleCondition("Baru")
                .vehicleYear(2025)
                .totalLoanAmount(new BigDecimal("100000000.00"))
                .downPayment(new BigDecimal("35000000.00"))
                .principalAmount(new BigDecimal("65000000.00"))
                .loanTenure(3)
                .yearlyInstallments(Collections.emptyList())
                .averageMonthlyInstallment(new BigDecimal("2028975.00"))
                .build();
    }

    @Test
    @DisplayName("Should successfully calculate credit simulation via POST /api/v1/credit/calculate")
    void calculateCredit_ValidPayload_Returns200OK() throws Exception {
        when(creditCalculatorService.calculateCredit(any(CreditSimulationRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/credit/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perhitungan simulasi kredit berhasil"))
                .andExpect(jsonPath("$.data.vehicleType").value("Mobil"))
                .andExpect(jsonPath("$.data.vehicleCondition").value("Baru"))
                .andExpect(jsonPath("$.data.loanTenure").value(3));

        verify(creditCalculatorService, times(1)).calculateCredit(any(CreditSimulationRequest.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when request payload validation fails via @Valid annotations")
    void calculateCredit_InvalidPayload_Returns400BadRequest() throws Exception {
        CreditSimulationRequest invalidRequest = new CreditSimulationRequest();

        mockMvc.perform(post("/api/v1/credit/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditCalculatorService);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when service throws ValidationException")
    void calculateCredit_BusinessValidationFails_Returns400BadRequest() throws Exception {
        when(creditCalculatorService.calculateCredit(any(CreditSimulationRequest.class)))
                .thenThrow(new ValidationException("Jumlah DP kurang dari batas minimal"));

        mockMvc.perform(post("/api/v1/credit/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(creditCalculatorService, times(1)).calculateCredit(any(CreditSimulationRequest.class));
    }

    @Test
    @DisplayName("Should successfully load data from external service via GET /api/v1/credit/load-external")
    void loadCalculateExternal_Success_Returns200OK() throws Exception {
        when(externalWebService.loadAndCalculateFromExternal()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/credit/load-external")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Berhasil memuat dan menghitung data dari external web service"))
                .andExpect(jsonPath("$.data.vehicleType").value("Mobil"));

        verify(externalWebService, times(1)).loadAndCalculateFromExternal();
    }
}