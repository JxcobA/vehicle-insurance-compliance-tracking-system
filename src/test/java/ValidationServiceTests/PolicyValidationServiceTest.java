package ValidationServiceTests;

import com.insurance.exceptions.PolicyValidationException;
import com.insurance.model.InsurancePolicy;
import com.insurance.service.PolicyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolicyValidationServiceTest {

    private PolicyValidationService validator;

    private final LocalDate issueDate = LocalDate.of(2026, 1, 1);
    private final LocalDate expiryDate = LocalDate.of(2027, 1, 1);

    @BeforeEach
    void setUp() {
        validator = new PolicyValidationService();
    }

    @Test
    void shouldPassForValidPolicy() {
        InsurancePolicy policy = new InsurancePolicy("RGF3 YNX", issueDate, expiryDate, "COMPREHENSIVE", true);

        assertDoesNotThrow(() -> validator.validatePolicy(policy));
    }

    @Test
    void shouldThrowWhenPolicyIsNull() {
        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(null));
    }

    @Test
    void shouldThrowWhenRegNumberIsBlank() {
        InsurancePolicy policy = new InsurancePolicy("        ", issueDate, expiryDate, "COMPREHENSIVE", true);

        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(policy));
    }

    @Test
    void shouldThrowWhenPolicyTypeIsNull() {
        InsurancePolicy policy = new InsurancePolicy("RGF3 YNX", issueDate, expiryDate, null, true);

        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(policy));
    }

    @Test
    void shouldThrowWhenIssueDateIsNull() {
        InsurancePolicy policy = new InsurancePolicy("RGF3 YNX", null, expiryDate, "COMPREHENSIVE", true);

        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(policy));
    }

    @Test
    void shouldThrowWhenExpiryIsBeforeIssue() {
        // Expiry one day before issue — violates the date rule
        InsurancePolicy policy = new InsurancePolicy("RGF3 YNX", issueDate, issueDate.minusDays(1), "COMPREHENSIVE", true);

        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(policy));
    }

    @Test
    void shouldThrowWhenExpiryEqualsIssue() {
        // Same date: "isAfter" check means equal dates should also fail
        InsurancePolicy policy = new InsurancePolicy("RGF3 YNX", issueDate, issueDate, "COMPREHENSIVE", true);

        assertThrows(PolicyValidationException.class, () -> validator.validatePolicy(policy));
    }

}
