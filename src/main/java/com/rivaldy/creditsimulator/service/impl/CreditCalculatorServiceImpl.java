package com.rivaldy.creditsimulator.service.impl;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.dto.response.YearlyInstallmentDetailResponse;
import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.util.helper.VehicleCondition;
import com.rivaldy.creditsimulator.util.helper.VehicleType;
import com.rivaldy.creditsimulator.util.validator.CreditRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCalculatorServiceImpl implements CreditCalculatorService {

    private final CreditRequestValidator validator;

    @Override
    public CreditSimulationResponse calculateCredit(CreditSimulationRequest request) {

        validator.validate(request);

        VehicleType vehicleType = VehicleType.fromString(request.getVehicleType());
        VehicleCondition vehicleCondition = VehicleCondition.fromString(request.getVehicleCondition());

        BigDecimal totalLoanAmount = request.getTotalLoanAmount();
        BigDecimal downPayment = request.getDownPayment();
        BigDecimal principalAmount = totalLoanAmount.subtract(downPayment);
        int tenureYears = request.getLoanTenure();

        List<YearlyInstallmentDetailResponse> yearlyInstallments = new ArrayList<>();
        BigDecimal currentPrincipalAmount = principalAmount;
        double currentInterestRate = vehicleType.getBaseInterestRate();
        BigDecimal totalMonthlyInstallmentsSum = BigDecimal.ZERO;

        for (int year = 1; year <= tenureYears; year++) {
            if (year > 1){
                if (year % 2 == 0){
                    currentInterestRate += 0.1;
                } else {
                    currentInterestRate += 0.5;
                }
            }

            currentInterestRate = BigDecimal.valueOf(currentInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            int remainingTenureYears = tenureYears - year + 1;
            BigDecimal rateMultiplier = BigDecimal.ONE.add(
                    BigDecimal.valueOf(currentInterestRate).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
            );
            BigDecimal totalLoanCurrentYear = currentPrincipalAmount.multiply(rateMultiplier)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal yearlyInstallment = totalLoanCurrentYear.divide(
                    BigDecimal.valueOf(remainingTenureYears), 2, RoundingMode.HALF_UP
            );
            BigDecimal monthlyInstallment = yearlyInstallment.divide(
                    BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP
            );

            yearlyInstallments.add(
                    YearlyInstallmentDetailResponse.builder()
                            .year(year)
                            .principalAmount(currentPrincipalAmount.setScale(2, RoundingMode.HALF_UP))
                            .interestRate(currentInterestRate)
                            .totalLoanAmount(totalLoanCurrentYear)
                            .monthlyInstallment(monthlyInstallment)
                            .yearlyInstallment(yearlyInstallment)
                            .build()
            );

            totalMonthlyInstallmentsSum = totalMonthlyInstallmentsSum.add(monthlyInstallment);

            currentPrincipalAmount = totalLoanCurrentYear.subtract(yearlyInstallment);
        }

        BigDecimal averageMonthlyInstallment = totalMonthlyInstallmentsSum.divide(
                BigDecimal.valueOf(tenureYears), 2, RoundingMode.HALF_UP
        );

        return CreditSimulationResponse.builder()
                .vehicleType(vehicleType.getDisplayName())
                .vehicleCondition(vehicleCondition.getDisplayName())
                .vehicleYear(request.getVehicleYear())
                .totalLoanAmount(totalLoanAmount.setScale(2, RoundingMode.HALF_UP))
                .downPayment(downPayment.setScale(2, RoundingMode.HALF_UP))
                .principalAmount(principalAmount.setScale(2, RoundingMode.HALF_UP))
                .loanTenure(tenureYears)
                .yearlyInstallments(yearlyInstallments)
                .averageMonthlyInstallment(averageMonthlyInstallment)
                .build();
    }
}
