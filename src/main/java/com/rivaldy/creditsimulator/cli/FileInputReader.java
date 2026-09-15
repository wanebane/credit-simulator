package com.rivaldy.creditsimulator.cli;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class FileInputReader {

    private final ObjectMapper objectMapper;

    public CreditSimulationRequest readRequest(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()){
            throw new ValidationException("Path file input tidak boleh kosong");
        }

        File file = new File(filePath.trim());
        if (!file.exists()){
            throw new ValidationException("File tidak ditemukan pada path : " + filePath);
        }
        Properties props = new Properties();
        try(InputStream input = new FileInputStream(file)){
            props.load(input);
        }

        CreditSimulationRequest request = new CreditSimulationRequest();
        request.setVehicleType(props.getProperty("vehicleType").trim());
        request.setVehicleCondition(props.getProperty("vehicleCondition").trim());
        if (props.getProperty("vehicleYear") != null){
            request.setVehicleYear(Integer.parseInt(props.getProperty("vehicleYear").trim()));
        }
        if (props.getProperty("totalLoanAmount") != null){
            request.setTotalLoanAmount(new BigDecimal(props.getProperty("totalLoanAmount").trim()));
        }
        if (props.getProperty("loanTenure") != null){
            request.setLoanTenure(Integer.parseInt(props.getProperty("loanTenure").trim()));
        }
        if (props.getProperty("downPayment") != null){
            request.setDownPayment(new BigDecimal(props.getProperty("downPayment").trim()));
        }
        return request;
    }
}
