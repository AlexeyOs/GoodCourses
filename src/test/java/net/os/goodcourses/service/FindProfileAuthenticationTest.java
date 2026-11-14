package net.os.goodcourses.service;

import net.os.goodcourses.entity.PasswordState;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.impl.FindProfileServiceImpl;
import net.os.goodcourses.model.CurrentProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindProfileAuthenticationTest {
    @Mock
    private ProfileRepository profileRepository;

    private FindProfileServiceImpl service;
    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        passwordService = new PasswordService(new BCryptPasswordEncoder(4));
        service = new FindProfileServiceImpl();
        ReflectionTestUtils.setField(service, "profileRepository", profileRepository);
    }

    @Test
    public void shouldLoadActiveProfileWithBcryptCredential() {
        Profile profile = profile(PasswordState.ACTIVE, passwordService.encode("Secret-123"));
        when(profileRepository.findByUid("alex")).thenReturn(Optional.of(profile));

        UserDetails user = service.loadUserByUsername("alex");

        assertEquals("alex", user.getUsername());
        assertEquals(profile.getPassword(), user.getPassword());
    }

    @Test
    public void shouldRejectProfileThatRequiresPasswordReset() {
        Profile profile = profile(PasswordState.RESET_REQUIRED, null);
        when(profileRepository.findByUid("alex")).thenReturn(Optional.of(profile));

        assertLocalLoginRejected();
    }

    @Test
    public void shouldRejectExternalOnlyProfileForLocalLogin() {
        Profile profile = profile(PasswordState.EXTERNAL_ONLY, null);
        when(profileRepository.findByUid("alex")).thenReturn(Optional.of(profile));

        assertLocalLoginRejected();
    }

    @Test
    public void shouldRejectLegacyPlaintextEvenWhenStateIsActive() {
        Profile profile = profile(PasswordState.ACTIVE, "Secret-123");
        when(profileRepository.findByUid("alex")).thenReturn(Optional.of(profile));

        assertLocalLoginRejected();
    }

    @Test
    public void externalOnlyProfileCanStillBeRepresentedAfterExternalAuthentication() {
        CurrentProfile currentProfile = new CurrentProfile(
                profile(PasswordState.EXTERNAL_ONLY, null));

        assertEquals("alex", currentProfile.getUsername());
    }

    private void assertLocalLoginRejected() {
        try {
            service.loadUserByUsername("alex");
            fail("UsernameNotFoundException expected");
        } catch (UsernameNotFoundException expected) {
            assertEquals("Local authentication is not available for this profile", expected.getMessage());
        }
    }

    private Profile profile(PasswordState state, String password) {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUid("alex");
        profile.setFirstName("Alex");
        profile.setLastName("Smith");
        profile.setPasswordState(state);
        profile.setPassword(password);
        return profile;
    }
}
