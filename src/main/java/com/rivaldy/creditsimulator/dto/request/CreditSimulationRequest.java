package com.rivaldy.creditsimulator.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditSimulationRequest {

    @NotBlank(message = "Jenis kendaraan tidak boleh kosong (Mobil/Motor)")
    private String vehicleType;

    @NotBlank(message = "Kondisi kendaraan tidak boleh kosong (Mobil/Motor)")
    private String vehicleCondition;

    @NotNull(message = "Tahun kendaraan tidak boleh kosong")
    @Min(value = 1900, message = "Tahun kendaraan tidak valid")
    @Max(value = 2100, message = "Tahun kendaraan tidak valid")
    private Integer vehicleYear;

    @NotNull(message = "Total pinjaman total tidak boleh kosong")
    @Min(value = 1, message = "Total pinjaman harus lebih dari 0")
    private BigDecimal totalLoanAmount;

    @NotNull(message = "Tenor pinjaman tidak boleh kosong")
    @Min(value = 1, message = "Tenor pinjaman minimal 1 tahun")
    @Max(value = 6, message = "Tenor pinjamna maksimal 6 tahun")
    private Integer loanTenure;

    @NotNull(message = "Uang muka tidak boleh kosong")
    @Min(value = 0, message = "Uang muka tidak boleh negatif")
    private BigDecimal downPayment;
}
