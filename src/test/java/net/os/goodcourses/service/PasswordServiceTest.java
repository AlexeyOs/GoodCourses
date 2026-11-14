package net.os.goodcourses.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PasswordServiceTest {
    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        passwordService = new PasswordService(new BCryptPasswordEncoder(4));
    }

    @Test
    public void shouldEncodePasswordWithBcrypt() {
        String encoded = passwordService.encode("Secret-123");

        assertTrue(PasswordService.isBcryptHash(encoded));
        assertFalse(encoded.contains("Secret-123"));
    }

    @Test
    public void shouldAcceptExactlySeventyTwoUtf8Bytes() {
        assertTrue(PasswordService.isBcryptHash(passwordService.encode(repeat("я", 36))));
    }

    @Test
    public void shouldRejectMoreThanSeventyTwoUtf8Bytes() {
        assertInvalid(repeat("я", 36) + "a");
    }

    @Test
    public void shouldRejectUnicodePasswordThatExceedsByteLimitDespiteShortCharacterCount() {
        assertInvalid(repeat("😀", 19));
    }

    @Test
    public void shouldRejectEmptyAndNullPasswords() {
        assertInvalid("");
        assertInvalid(null);
    }

    @Test
    public void shouldNotRecognizePlaintextAsBcryptHash() {
        assertFalse(PasswordService.isBcryptHash("legacy-plaintext-password"));
        assertFalse(PasswordService.isBcryptHash(null));
    }

    private void assertInvalid(String password) {
        try {
            passwordService.encode(password);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private String repeat(String value, int count) {
        StringBuilder result = new StringBuilder(value.length() * count);
        for (int i = 0; i < count; i++) {
            result.append(value);
        }
        return result.toString();
    }
}
