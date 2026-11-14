package net.os.goodcourses.service;

import net.os.goodcourses.entity.PasswordState;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.form.SignUpForm;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.impl.AddProfileServiceImpl;
import net.os.goodcourses.service.impl.EditProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        passwordService = new PasswordService(new BCryptPasswordEncoder(4));
        when(profileRepository.save(any(Profile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void addProfileShouldStoreBcryptAndActivatePassword() {
        AddProfileServiceImpl service = new AddProfileServiceImpl();
        ReflectionTestUtils.setField(service, "profileRepository", profileRepository);
        ReflectionTestUtils.setField(service, "passwordService", passwordService);

        Profile profile = service.createNewProfile(
                "Alex", "Smith", "alex", "alex@example.com", "Secret-123");

        assertEquals(PasswordState.ACTIVE, profile.getPasswordState());
        assertTrue(PasswordService.isBcryptHash(profile.getPassword()));
        assertTrue(new BCryptPasswordEncoder().matches("Secret-123", profile.getPassword()));
        verify(profileRepository).save(profile);
    }

    @Test
    public void editProfileCreationShouldStoreBcryptAndActivatePassword() {
        EditProfileServiceImpl service = editProfileService();
        ReflectionTestUtils.setField(service, "generateUidAlphabet", "abcdef");
        ReflectionTestUtils.setField(service, "generateUidSuffixLength", 4);
        ReflectionTestUtils.setField(service, "maxTryCountToGenerateUid", 3);
        SignUpForm form = signUpForm("Secret-123");

        TransactionSynchronizationManager.initSynchronization();
        Profile profile;
        try {
            profile = service.createNewProfile(form);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }

        assertEquals(PasswordState.ACTIVE, profile.getPasswordState());
        assertTrue(new BCryptPasswordEncoder().matches("Secret-123", profile.getPassword()));
    }

    @Test
    public void passwordChangeShouldReplaceResetRequiredStateWithActiveBcrypt() {
        EditProfileServiceImpl service = editProfileService();
        Profile profile = new Profile();
        profile.setPassword(null);
        profile.setPasswordState(PasswordState.RESET_REQUIRED);

        service.updatePassword(profile, "Changed-123");

        assertEquals(PasswordState.ACTIVE, profile.getPasswordState());
        assertTrue(new BCryptPasswordEncoder().matches("Changed-123", profile.getPassword()));
        verify(profileRepository).save(profile);
    }

    private EditProfileServiceImpl editProfileService() {
        EditProfileServiceImpl service = new EditProfileServiceImpl();
        ReflectionTestUtils.setField(service, "profileRepository", profileRepository);
        ReflectionTestUtils.setField(service, "passwordService", passwordService);
        return service;
    }

    private SignUpForm signUpForm(String password) {
        SignUpForm form = new SignUpForm();
        form.setFirstName("Alex");
        form.setLastName("Smith");
        form.setPassword(password);
        form.setConfirmPassword(password);
        return form;
    }
}
