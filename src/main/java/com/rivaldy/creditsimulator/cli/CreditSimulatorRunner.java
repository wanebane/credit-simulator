package com.rivaldy.creditsimulator.cli;

import com.rivaldy.creditsimulator.dto.request.CreditSimulationRequest;
import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.util.display.CreditSimulatorCliDisplay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditSimulatorRunner implements CommandLineRunner {

    private final FileInputReader fileInputReader;
    private final CreditCalculatorService creditCalculatorService;
    private final CreditSimulatorCliDisplay cliDisplay;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0 || args[0].isBlank()){
            log.info("CLI Runner skipped: Tidak ada argumen path file yang diberikan.");
            return;
        }
        String filePath = args[0];
        File txtFile = new File(filePath);

        if (!txtFile.exists()){
            log.error("File .txt tidak ditemukan di path : {}", filePath);
            return;
        }
        try {
            log.info("Membaca data simulasi dari file .txt : {}", filePath);
            CreditSimulationRequest request = fileInputReader.readRequest(filePath);
            CreditSimulationResponse response = creditCalculatorService.calculateCredit(request);
            cliDisplay.displayResult(response);
        } catch (IOException ex){
            log.error("Gagal menjalankan simulasi via CLI : {}", ex.getMessage());
        } finally {
            log.info("Selesai membuat data simulasi");
            System.exit(0);
        }
    }




















}
