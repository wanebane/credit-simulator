package com.rivaldy.creditsimulator.service;

import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;

public interface ExternalWebService {
    CreditSimulationResponse loadAndCalculateFromExternal();
}
