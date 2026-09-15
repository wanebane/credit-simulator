package com.rivaldy.creditsimulator.service;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;

public interface CreditCalculatorService {

    CreditSimulationResponse calculateCredit(CreditSimulationRequest request);
}
