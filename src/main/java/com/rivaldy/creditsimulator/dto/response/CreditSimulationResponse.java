package com.rivaldy.creditsimulator.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditSimulationResponse {
    private String vehicleType;
    private String vehicleCondition;
    private Integer vehicleYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalLoanAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal downPayment;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal principalAmount;
    private Integer loanTenure;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal averageMonthlyInstallment;
    private List<YearlyInstallmentDetailResponse> yearlyInstallments;
}
