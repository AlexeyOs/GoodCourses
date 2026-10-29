package net.os.goodcourses.form;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeedbackFormTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    public void shouldRejectNullDescription() {
        assertDescriptionInvalid(null);
    }

    @Test
    public void shouldRejectEmptyDescription() {
        assertDescriptionInvalid("");
    }

    @Test
    public void shouldRejectWhitespaceDescription() {
        assertDescriptionInvalid("   \t\r\n");
    }

    @Test
    public void shouldAcceptDescriptionWithTwoThousandCharacters() {
        Set<ConstraintViolation<FeedbackForm>> violations = validate(repeat('a', 2000));

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldRejectDescriptionWithTwoThousandAndOneCharacters() {
        Set<ConstraintViolation<FeedbackForm>> violations = validate(repeat('a', 2001));

        assertEquals(1, violations.size());
        assertEquals("description", violations.iterator().next().getPropertyPath().toString());
    }

    private void assertDescriptionInvalid(String description) {
        Set<ConstraintViolation<FeedbackForm>> violations = validate(description);

        assertTrue("Description must be invalid", !violations.isEmpty());
        for (ConstraintViolation<FeedbackForm> violation : violations) {
            assertEquals("description", violation.getPropertyPath().toString());
        }
    }

    private Set<ConstraintViolation<FeedbackForm>> validate(String description) {
        return validator.validate(new FeedbackForm(description));
    }

    private String repeat(char value, int count) {
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append(value);
        }
        return result.toString();
    }
}
