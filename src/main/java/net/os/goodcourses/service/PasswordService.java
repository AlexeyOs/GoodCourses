package net.os.goodcourses.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Service
public class PasswordService {
    public static final int BCRYPT_MAX_PASSWORD_BYTES = 72;

    private static final Pattern BCRYPT_HASH = Pattern.compile(
            "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String rawPassword) {
        validate(rawPassword);
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean isBcryptHash(String password) {
        return password != null && BCRYPT_HASH.matcher(password).matches();
    }

    private void validate(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (rawPassword.getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_PASSWORD_BYTES) {
            throw new IllegalArgumentException("Пароль не должен превышать 72 байта в UTF-8");
        }
    }
}
