package com.insurance.util;

import com.insurance.model.ViolationRecord;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvReportGenerator {

    public String generateViolationReport(List<ViolationRecord> violations) {
        String fileName = "vehicle_insurance_violations_" +
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv";

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.append("Registration,Expiry Date,Last Start,Last End,Reason\n");

            for (ViolationRecord v : violations) {
                writer.append(safe(v.getRegNumber())).append(",");
                writer.append(safe(String.valueOf(v.getExpiryDate()))).append(",");
                writer.append(safe(String.valueOf(v.getLastTripStart()))).append(",");
                writer.append(safe(String.valueOf(v.getLastTripEnd()))).append(",");
                writer.append(safe(v.getReason())).append("\n");
            }

            System.out.println("CSV created: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

