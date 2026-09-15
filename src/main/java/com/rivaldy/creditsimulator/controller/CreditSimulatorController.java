package com.rivaldy.creditsimulator.controller;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.ApiResponse;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.service.ExternalWebService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditSimulatorController {

    private final CreditCalculatorService creditCalculatorService;
    private final ExternalWebService externalWebService;

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<CreditSimulationResponse>> calculateCredit(
            @Valid @RequestBody CreditSimulationRequest request
    ){
        CreditSimulationResponse response = creditCalculatorService.calculateCredit(request);
        return ResponseEntity.ok(ApiResponse.success("Perhitungan simulasi kredit berhasil", response));
    }

    @GetMapping("/load-external")
    public ResponseEntity<ApiResponse<CreditSimulationResponse>> loadCalculateExternal(){
        CreditSimulationResponse response = externalWebService.loadAndCalculateFromExternal();
        return ResponseEntity.ok(ApiResponse.success("Berhasil memuat dan menghitung data dari external web service", response));
    }

}
