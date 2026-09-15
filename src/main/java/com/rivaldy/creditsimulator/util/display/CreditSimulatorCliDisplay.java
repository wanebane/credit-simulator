package com.rivaldy.creditsimulator.util.display;

import com.rivaldy.creditsimulator.dto.response.CreditSimulationResponse;
import com.rivaldy.creditsimulator.dto.response.YearlyInstallmentDetailResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Component
public class CreditSimulatorCliDisplay {

    public void displayResult(CreditSimulationResponse response){
        List<YearlyInstallmentDetailResponse> installments = response.getYearlyInstallments();
        int totalYears = installments.size();

        int labelWidth = 20;

        int[] colWidths = new int[totalYears];
        for (int i = 0; i < totalYears; i++) {
            YearlyInstallmentDetailResponse detail = installments.get(i);
            int maxLen = ("Tahun " + detail.getYear()).length();

            maxLen = Math.max(maxLen, formatRupiah(detail.getPrincipalAmount()).length());
            maxLen = Math.max(maxLen, String.format("%.2f%%", detail.getInterestRate()).length());
            maxLen = Math.max(maxLen, formatRupiah(detail.getTotalLoanAmount()).length());
            maxLen = Math.max(maxLen, formatRupiah(detail.getMonthlyInstallment()).length());
            maxLen = Math.max(maxLen, formatRupiah(detail.getYearlyInstallment()).length());

            colWidths[i] = Math.max(maxLen + 2, 18);
        }

        int totalTableWidth = labelWidth + 3;
        for (int width : colWidths){
            totalTableWidth += width + 3;
        }

        String borderLine = "=".repeat(totalTableWidth);
        String dashLine = "-".repeat(totalTableWidth);

        System.out.println(borderLine);
        System.out.println(centerText("HASIL SIMULASI KREDIT", totalTableWidth));
        System.out.println(borderLine);

        System.out.printf("%-25s : %s%n", "Jenis Kendaraan", response.getVehicleType());
        System.out.printf("%-25s : %s%n", "Kondisi Kendaraan", response.getVehicleCondition());
        System.out.printf("%-25s : %d%n", "Tahun Kendaraan", response.getVehicleYear());
        System.out.printf("%-25s : %s%n", "Total Pinjaman", formatRupiah(response.getTotalLoanAmount()));
        System.out.printf("%-25s : %s%n", "Uang Muka (DP)", formatRupiah(response.getDownPayment()));
        System.out.printf("%-25s : %s%n", "Pokok Pinjaman", formatRupiah(response.getPrincipalAmount()));
        System.out.printf("%-25s : %d Tahun%n", "Tenor", response.getLoanTenure());
        System.out.printf("%-25s : %s%n", "Rata-rata / bulan", formatRupiah(response.getAverageMonthlyInstallment()));

        System.out.println("\n"+dashLine);
        System.out.println(centerText("RINCIAN ANGSURAN PER TAHUN", totalTableWidth));
        System.out.println(dashLine);

        StringBuilder header = new StringBuilder();
        header.append(String.format("%-" + labelWidth + "s", "Komponen"));
        for (int i = 0; i < totalYears; i++) {
            header.append(" | ").append(String.format("%-" + colWidths[i] + "s", "Tahun " + installments.get(i).getYear()));
        }
        System.out.println(header);
        System.out.println(dashLine);

        displayRow(labelWidth, colWidths, installments, "Pokok Pinjaman", d -> formatRupiah(d.getPrincipalAmount()));
        displayRow(labelWidth, colWidths, installments, "Rate", d -> String.format("%.2f%%", d.getInterestRate()));
        displayRow(labelWidth, colWidths, installments, "Total Pinjaman", d -> formatRupiah(d.getTotalLoanAmount()));
        displayRow(labelWidth, colWidths, installments, "Angsuran / bulan", d -> formatRupiah(d.getMonthlyInstallment()));
        displayRow(labelWidth, colWidths, installments, "Angsuran / tahun", d -> formatRupiah(d.getYearlyInstallment()));

        System.out.println(borderLine);
    }

    private String formatRupiah(BigDecimal amount){
        if (amount == null){
            return "Rp. 0,00";
        }

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp. ");
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(amount);
    }

    private String centerText(String text, int width){
        int padding = (width - text.length()) / 2;
        if (padding <= 0)
            return text;
        return " ".repeat(padding) + text;
    }

    private void displayRow(int labelWidth, int[] colWidths, List<YearlyInstallmentDetailResponse> list,
                            String label, Function<YearlyInstallmentDetailResponse, String> valueExtractor){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-" + labelWidth + "s", label));

        for (int i = 0; i < list.size(); i++) {
            String extract = valueExtractor.apply(list.get(i));
            sb.append(" | ").append(String.format("%" + colWidths[i] + "s", extract));
        }
        System.out.println(sb);
    }
}
