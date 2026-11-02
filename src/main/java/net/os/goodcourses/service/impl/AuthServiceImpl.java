package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.entity.ProfileRestore;
import net.os.goodcourses.entity.PasswordState;
import net.os.goodcourses.repository.storage.ProfileRestoreRepository;
import net.os.goodcourses.service.AuthService;
import net.os.goodcourses.service.EditProfileService;
import net.os.goodcourses.service.FindProfileService;
import net.os.goodcourses.service.NotificationManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String RESET_RESPONSE = "Вам будет отправлено сообщение, проверьте почту";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final FindProfileService findProfileService;
    private final EditProfileService editProfileService;
    private final NotificationManagerService notificationManagerService;
    private final ProfileRestoreRepository profileRestoreRepository;
    private final String appHost;
    private final long tokenTtlMinutes;

    public AuthServiceImpl(FindProfileService findProfileService,
                           EditProfileService editProfileService,
                           NotificationManagerService notificationManagerService,
                           ProfileRestoreRepository profileRestoreRepository,
                           @Value("${app.host}") String appHost,
                           @Value("${password.recovery.ttl.minutes:30}") long tokenTtlMinutes) {
        this.findProfileService = findProfileService;
        this.editProfileService = editProfileService;
        this.notificationManagerService = notificationManagerService;
        this.profileRestoreRepository = profileRestoreRepository;
        this.appHost = removeTrailingSlash(appHost);
        if (tokenTtlMinutes <= 0) {
            throw new IllegalArgumentException("Password recovery token TTL must be positive");
        }
        this.tokenTtlMinutes = tokenTtlMinutes;
    }

    @Override
    @Transactional
    public String passwordReset(String mailOrLogin, Model model) {
        Optional<Profile> profile = findProfile(mailOrLogin);
        if (profile.isPresent() && profile.get().getPasswordState() != PasswordState.EXTERNAL_ONLY) {
            issueToken(profile.get());
        }
        model.addAttribute("alertMsg", RESET_RESPONSE);
        return "password-reset";
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordResetTokenValid(long profileId, String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Optional<ProfileRestore> restore = profileRestoreRepository
                .findByIdAndTokenHash(profileId, hashToken(token));
        return restore.isPresent() && !isExpired(restore.get());
    }

    @Override
    @Transactional
    public boolean changePassword(long profileId, String token, String password) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Optional<ProfileRestore> restoreOptional = profileRestoreRepository.findByIdForUpdate(profileId);
        if (!restoreOptional.isPresent()) {
            return false;
        }
        ProfileRestore restore = restoreOptional.get();
        if (isExpired(restore) || !hashesEqual(restore.getTokenHash(), hashToken(token))) {
            return false;
        }

        Profile profile = restore.getProfile();
        if (profile.getPasswordState() == PasswordState.EXTERNAL_ONLY) {
            return false;
        }
        editProfileService.updatePassword(profile, password);
        profileRestoreRepository.delete(restore);
        notificationManagerService.sendPasswordChanged(profile);
        return true;
    }

    private Optional<Profile> findProfile(String mailOrLogin) {
        if (mailOrLogin == null) {
            return Optional.empty();
        }
        return mailOrLogin.contains("@")
                ? findProfileService.findByEmail(mailOrLogin)
                : findProfileService.findByUid(mailOrLogin);
    }

    private void issueToken(Profile profile) {
        byte[] tokenBytes = new byte[32];
        SECURE_RANDOM.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        ProfileRestore restore = profileRestoreRepository.findById(profile.getId())
                .orElseGet(ProfileRestore::new);
        restore.setId(profile.getId());
        restore.setProfile(profile);
        restore.setTokenHash(hashToken(token));
        restore.setExpiresAt(Timestamp.from(
                Instant.now().plus(tokenTtlMinutes, ChronoUnit.MINUTES)));
        profileRestoreRepository.save(restore);

        String restoreLink = appHost + "/password-change#token=" + token +
                "&profileId=" + profile.getId();
        notificationManagerService.sendRestoreAccessLink(profile, restoreLink);
    }

    private boolean isExpired(ProfileRestore restore) {
        return restore.getExpiresAt() == null ||
                !restore.getExpiresAt().toInstant().isAfter(Instant.now());
    }

    private static String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte value : digest) {
                hex.append(String.format("%02x", value & 0xff));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private static boolean hashesEqual(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.US_ASCII),
                actual.getBytes(StandardCharsets.US_ASCII));
    }

    private static String removeTrailingSlash(String host) {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("app.host must be configured");
        }
        String normalized = host.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
