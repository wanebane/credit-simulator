package com.rivaldy.creditsimulator.util.validator;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.exception.ValidationException;
import com.rivaldy.creditsimulator.util.helper.VehicleCondition;
import com.rivaldy.creditsimulator.util.helper.VehicleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;

@Component
public class CreditRequestValidator {

    private static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("10000000000.0");

    public void validate(CreditSimulationRequest request){
        VehicleType vehicleType = VehicleType.fromString(request.getVehicleType());
        if (vehicleType == null){
            throw new ValidationException("Jenis kendaraan harus 'Mobil' atau 'Motor'");
        }

        VehicleCondition vehicleCondition = VehicleCondition.fromString(request.getVehicleCondition());
        if (vehicleCondition == null){
            throw new ValidationException("Kondisi kendaraan harus 'Baru' atau 'Bekas'");
        }

        if (request.getTotalLoanAmount().compareTo(MAX_LOAN_AMOUNT) > 0){
            throw new ValidationException("Jumlah pinjaman total tidak boleh lebih dari Rp 10.000.000.000");
        }

        if (request.getLoanTenure() < 1 || request.getLoanTenure() > 6){
            throw new ValidationException("Tenor pinjaman harus antara 1 sampai 6 tahun");
        }

        int currentYear = Year.now().getValue();
        int validateYear = currentYear - 1;
        if (vehicleCondition == VehicleCondition.BARU && request.getVehicleYear() < validateYear){
            throw new ValidationException("Kendaraan dengan kondisi BARU tidak boleh diinput tahun kurang dari " + validateYear);
        }

        double minDpPercentage = vehicleCondition.getMinimumDpPercentage();
        BigDecimal minDpPercentageBD = BigDecimal.valueOf(minDpPercentage);
        BigDecimal minDpAmount = request.getTotalLoanAmount().multiply(minDpPercentageBD);
        if (request.getDownPayment().compareTo(minDpAmount) < 0){
            throw new ValidationException(
                    String.format(
                            "Jumlah DP untuk kendaraan %s minimal %.0f%% dari total pinjaman (Minimal: Rp %,.2f)",
                            vehicleCondition.name(), (minDpPercentage * 100), minDpAmount
                    )
            );
        }
    }
}
