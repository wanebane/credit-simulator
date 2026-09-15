package com.rivaldy.creditsimulator.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearlyInstallmentDetailResponse {
    private Integer year;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal principalAmount;
    private Double interestRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalLoanAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal monthlyInstallment;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal yearlyInstallment;
}
