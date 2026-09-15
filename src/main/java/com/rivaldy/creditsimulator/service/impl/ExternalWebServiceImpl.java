package com.rivaldy.creditsimulator.service.impl;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.exception.ValidationException;
import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.service.ExternalWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalWebServiceImpl implements ExternalWebService {

    private final RestTemplate restTemplate;
    private final CreditCalculatorService creditCalculatorService;

    @Value("${eternal.mocky.url:https://run.mocky.io/v3/9108b1da-beec-409e-ae14-e8091955666c}")
    private String externalUrl;

    @Override
    public CreditSimulationResponse loadAndCalculateFromExternal() {
        try{
            CreditSimulationRequest resultExternal = restTemplate.getForObject(externalUrl, CreditSimulationRequest.class);
            if (resultExternal == null){
                throw new ValidationException("Gagal mengambil data simulasi dari external web service");
            }
            return creditCalculatorService.calculateCredit(resultExternal);

        } catch (Exception ex){
            throw new ValidationException("Terjadi kesalahan saat memanggil API external : " + ex.getMessage());
        }
    }
}
