package UtilTests;

import com.insurance.model.ViolationRecord;
import com.insurance.util.CsvReportGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvReportGeneratorTests {

    private CsvReportGenerator generator;
    private String fileName;

    @BeforeEach
    void setUp() {
        generator = new CsvReportGenerator();
    }

    @AfterEach
    void tearDown() {
        if (fileName != null) {
            new File(fileName).delete();
        }
    }


    private ViolationRecord sampleViolation() {
        return new ViolationRecord(
                "TEST1",
                LocalDate.of(2025, 1, 1),
                LocalDateTime.of(2025, 3, 1, 10, 0),
                LocalDateTime.of(2025, 3, 1, 12, 0),
                "Vehicle used after insurance expiry"
        );
    }

    @Test
    void shouldReturnNonNullFileName() {
        // Generate report with one violation, store filename
        fileName = generator.generateViolationReport(List.of(sampleViolation()));
        assertNotNull(fileName); // Assert a filename was returned
    }

    @Test
    void shouldCreateCsvFile() {
        fileName = generator.generateViolationReport(List.of(sampleViolation()));
        assertTrue(new File(fileName).exists()); // Use File to check the CSV was actually created on disk

    }

    @Test
    void shouldCreateFileWithNoViolations() {
        fileName = generator.generateViolationReport(List.of());
        assertTrue(new File(fileName).exists());
    }



}
