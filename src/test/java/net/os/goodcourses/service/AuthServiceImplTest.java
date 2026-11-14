package net.os.goodcourses.service;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.entity.ProfileRestore;
import net.os.goodcourses.repository.storage.ProfileRestoreRepository;
import net.os.goodcourses.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    private static final long PROFILE_ID = 42L;
    private static final String EXTERNAL_RESPONSE = "Вам будет отправлено сообщение, проверьте почту";

    @Mock private FindProfileService findProfileService;
    @Mock private EditProfileService editProfileService;
    @Mock private NotificationManagerService notificationManagerService;
    @Mock private ProfileRestoreRepository profileRestoreRepository;

    private AuthServiceImpl authService;
    private Profile profile;

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl(findProfileService, editProfileService,
                notificationManagerService, profileRestoreRepository,
                "https://courses.example/", 30);
        profile = new Profile();
        profile.setId(PROFILE_ID);
        profile.setUid("alex");
        profile.setEmail("alex@example.com");
        profile.setFirstName("Alex");
        profile.setLastName("Smith");
    }

    @Test
    public void shouldStoreOnlyTokenHashAndBuildLinkFromTrustedHost() throws Exception {
        when(findProfileService.findByEmail("alex@example.com")).thenReturn(Optional.of(profile));
        when(profileRestoreRepository.findById(PROFILE_ID)).thenReturn(Optional.empty());
        Model model = new ExtendedModelMap();
        Instant before = Instant.now();

        String view = authService.passwordReset("alex@example.com", model);

        assertEquals("password-reset", view);
        assertEquals(EXTERNAL_RESPONSE, model.asMap().get("alertMsg"));
        ArgumentCaptor<ProfileRestore> restoreCaptor = ArgumentCaptor.forClass(ProfileRestore.class);
        verify(profileRestoreRepository).save(restoreCaptor.capture());
        ProfileRestore restore = restoreCaptor.getValue();
        assertEquals(Long.valueOf(PROFILE_ID), restore.getId());
        assertEquals(profile, restore.getProfile());
        assertEquals(64, restore.getTokenHash().length());
        assertTrue(restore.getExpiresAt().toInstant().isAfter(before.plusSeconds(29 * 60)));

        ArgumentCaptor<String> linkCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationManagerService).sendRestoreAccessLink(any(Profile.class), linkCaptor.capture());
        String link = linkCaptor.getValue();
        assertTrue(link.startsWith("https://courses.example/password-change#token="));
        String rawToken = link.substring(link.indexOf("token=") + 6, link.indexOf("&profileId="));
        assertNotEquals(rawToken, restore.getTokenHash());
        assertEquals(restore.getTokenHash(), sha256(rawToken));
    }

    @Test
    public void shouldReturnSameExternalResponseForUnknownProfile() {
        when(findProfileService.findByUid("unknown")).thenReturn(Optional.empty());
        Model model = new ExtendedModelMap();

        String view = authService.passwordReset("unknown", model);

        assertEquals("password-reset", view);
        assertEquals(EXTERNAL_RESPONSE, model.asMap().get("alertMsg"));
        verify(profileRestoreRepository, never()).save(any(ProfileRestore.class));
        verify(notificationManagerService, never()).sendRestoreAccessLink(any(Profile.class), any(String.class));
    }

    @Test
    public void shouldValidateUnexpiredHashedToken() throws Exception {
        String rawToken = "one-time-secret";
        ProfileRestore restore = restore(sha256(rawToken), Instant.now().plusSeconds(60));
        when(profileRestoreRepository.findByIdAndTokenHash(PROFILE_ID, sha256(rawToken)))
                .thenReturn(Optional.of(restore));

        assertTrue(authService.isPasswordResetTokenValid(PROFILE_ID, rawToken));
    }

    @Test
    public void shouldRejectExpiredToken() throws Exception {
        String rawToken = "expired-secret";
        ProfileRestore restore = restore(sha256(rawToken), Instant.now().minusSeconds(1));
        when(profileRestoreRepository.findByIdAndTokenHash(PROFILE_ID, sha256(rawToken)))
                .thenReturn(Optional.of(restore));

        assertFalse(authService.isPasswordResetTokenValid(PROFILE_ID, rawToken));
    }

    @Test
    public void shouldConsumeTokenAfterSuccessfulPasswordChange() throws Exception {
        String rawToken = "one-time-secret";
        ProfileRestore restore = restore(sha256(rawToken), Instant.now().plusSeconds(60));
        when(profileRestoreRepository.findByIdForUpdate(PROFILE_ID))
                .thenReturn(Optional.of(restore), Optional.empty());

        assertTrue(authService.changePassword(PROFILE_ID, rawToken, "Changed-123"));
        assertFalse(authService.changePassword(PROFILE_ID, rawToken, "Changed-Again-123"));

        verify(editProfileService).updatePassword(profile, "Changed-123");
        verify(profileRestoreRepository).delete(restore);
        verify(notificationManagerService).sendPasswordChanged(profile);
    }

    @Test
    public void shouldRejectWrongTokenWithoutChangingPassword() throws Exception {
        ProfileRestore restore = restore(sha256("correct"), Instant.now().plusSeconds(60));
        when(profileRestoreRepository.findByIdForUpdate(PROFILE_ID)).thenReturn(Optional.of(restore));

        assertFalse(authService.changePassword(PROFILE_ID, "wrong", "Changed-123"));

        verify(editProfileService, never()).updatePassword(any(Profile.class), any(String.class));
        verify(profileRestoreRepository, never()).delete(any(ProfileRestore.class));
    }

    private ProfileRestore restore(String tokenHash, Instant expiresAt) {
        ProfileRestore restore = new ProfileRestore();
        restore.setId(PROFILE_ID);
        restore.setProfile(profile);
        restore.setTokenHash(tokenHash);
        restore.setExpiresAt(Timestamp.from(expiresAt));
        return restore;
    }

    private String sha256(String value) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte current : digest) {
            hex.append(String.format("%02x", current & 0xff));
        }
        return hex.toString();
    }
}
