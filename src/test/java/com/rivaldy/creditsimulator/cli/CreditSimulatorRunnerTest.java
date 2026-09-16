package com.rivaldy.creditsimulator.cli;

import com.rivaldy.creditsimulator.service.CreditCalculatorService;
import com.rivaldy.creditsimulator.util.display.CreditSimulatorCliDisplay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreditSimulatorRunnerTest {

    @Mock
    private FileInputReader fileInputReader;

    @Mock
    private CreditCalculatorService creditCalculatorService;

    @Mock
    private CreditSimulatorCliDisplay cliDisplay;

    @InjectMocks
    private CreditSimulatorRunner creditSimulatorRunner;

    @Test
    @DisplayName("Should skip execution when no command line arguments are provided")
    void run_NoArguments_SkipsExecution() throws Exception {
        creditSimulatorRunner.run();

        verifyNoInteractions(fileInputReader, creditCalculatorService, cliDisplay);
    }

    @Test
    @DisplayName("Should skip execution when passed argument is blank")
    void run_BlankArgument_SkipsExecution() throws Exception {
        creditSimulatorRunner.run("   ");

        verifyNoInteractions(fileInputReader, creditCalculatorService, cliDisplay);
    }

    @Test
    @DisplayName("Should log error and skip processing when file does not exist")
    void run_FileNotFound_SkipsProcessing() throws Exception {
        creditSimulatorRunner.run("input-file/non-existent.txt");

        verifyNoInteractions(fileInputReader, creditCalculatorService, cliDisplay);
    }
}