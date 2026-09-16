package com.rivaldy.creditsimulator.service.impl;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.dto.response.YearlyInstallmentDetailResponse;
import com.rivaldy.creditsimulator.util.validator.CreditRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCalculatorServiceImplTest {

    @Mock
    private CreditRequestValidator validator;

    @InjectMocks
    private CreditCalculatorServiceImpl creditCalculatorService;

    private CreditSimulationRequest requestNewCar;

    @BeforeEach
    void setUp() {
        requestNewCar = new CreditSimulationRequest();
        requestNewCar.setVehicleType("Mobil");
        requestNewCar.setVehicleCondition("Baru");
        requestNewCar.setVehicleYear(2025);
        requestNewCar.setTotalLoanAmount(new BigDecimal("100000000"));
        requestNewCar.setDownPayment(new BigDecimal("35000000"));
        requestNewCar.setLoanTenure(3);
    }

    @Test
    @DisplayName("Must success to calculate credit")
    void calculateCredit_Success() {
        doNothing().when(validator).validate(any(CreditSimulationRequest.class));

        CreditSimulationResponse response = creditCalculatorService.calculateCredit(requestNewCar);

        assertNotNull(response);
        assertEquals("Mobil", response.getVehicleType());
        assertEquals("Baru", response.getVehicleCondition());
        assertEquals(0, new BigDecimal("65000000.00").compareTo(response.getPrincipalAmount()));

        List<YearlyInstallmentDetailResponse> yearlyInstallments = response.getYearlyInstallments();
        assertEquals(3, yearlyInstallments.size());
        assertEquals(8.0, yearlyInstallments.get(0).getInterestRate());
        assertEquals(8.1, yearlyInstallments.get(1).getInterestRate());
        assertEquals(8.6, yearlyInstallments.get(2).getInterestRate());

        verify(validator, times(1)).validate(requestNewCar);
    }
}