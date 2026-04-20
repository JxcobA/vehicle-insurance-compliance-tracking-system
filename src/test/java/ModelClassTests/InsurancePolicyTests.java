package ModelClassTests;

import com.insurance.model.InsurancePolicy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InsurancePolicyTests {

    // Tests getters
    @Test
    void shouldStoreValuesOnConstruction() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);

        assertEquals(0, policy.getId()); // id is not set here, so defaults to 0
        assertEquals("TEST1", policy.getRegNumber());
        assertEquals(LocalDate.of(2024, 1, 1), policy.getIssueDate());
        assertEquals(LocalDate.of(2025, 1, 1), policy.getExpiryDate());
        assertEquals("COMPREHENSIVE", policy.getPolicyType());
        assertTrue(policy.isActive()); // This is functionally the same, but more optimised than the commented code below
        // assertEquals(true, policy.isActive());
    }

    @Test
    void shouldUpdateIdUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setId(1);
        assertEquals(1, policy.getId());
    }

    @Test
    void shouldUpdateRegNumberUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setRegNumber("TEST2");
        assertEquals("TEST2", policy.getRegNumber());
    }

    @Test
    void shouldUpdateIssueDateUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setIssueDate(LocalDate.of(2025, 1, 1));
        assertEquals(LocalDate.of(2025, 1, 1), policy.getIssueDate());
    }

    @Test
    void shouldUpdateExpiryDateUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setExpiryDate(LocalDate.of(2025, 1, 1));
        assertEquals(LocalDate.of(2025, 1, 1), policy.getExpiryDate());
    }

    @Test
    void shouldUpdatePolicyTypeUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setPolicyType("THIRD_PARTY");
        assertEquals("THIRD_PARTY", policy.getPolicyType());
    }

    @Test
    void shouldUpdateIsActiveUsingSetter() {
        InsurancePolicy policy = new InsurancePolicy("TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true);
        policy.setActive(false);
        assertFalse(policy.isActive());
    }
}
