package net.os.goodcourses.controller;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.AddProfileService;
import net.os.goodcourses.service.AuthService;
import net.os.goodcourses.service.EditProfileService;
import net.os.goodcourses.service.FindProfileService;
import net.os.goodcourses.service.NotificationManagerService;
import net.os.goodcourses.util.SecurityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @Mock private FindProfileService findProfileService;
    @Mock private AddProfileService addProfileService;
    @Mock private EditProfileService editProfileService;
    @Mock private NotificationManagerService notificationManagerService;
    @Mock private PersistentTokenRepository persistentTokenRepository;
    @Mock private AuthService authService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        SecurityContextHolder.clearContext();
        AuthController controller = new AuthController(findProfileService, addProfileService,
                editProfileService, notificationManagerService, persistentTokenRepository, authService);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/JSP/");
        viewResolver.setSuffix(".jsp");
        mockMvc = standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void guestShouldSeeSignInPage() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"));
    }

    @Test
    public void authenticatedUserShouldBeRedirectedFromSignInToOwnProfile() throws Exception {
        SecurityUtil.authentificate(profile(7L, "alex", "secret"));

        mockMvc.perform(get("/sign-in"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/alex"))
                .andExpect(redirectedUrl("/alex"));
    }

    @Test
    public void guestShouldSeeSignUpPage() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"));
    }

    @Test
    public void authenticatedUserShouldBeRedirectedFromSignUpToOwnProfile() throws Exception {
        SecurityUtil.authentificate(profile(7L, "alex", "secret"));

        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/alex"))
                .andExpect(redirectedUrl("/alex"));
    }

    @Test
    public void shouldCreateAndAuthenticateNewProfile() throws Exception {
        Profile created = profile(11L, "new-user", "secret");
        when(findProfileService.findByUid("new-user")).thenReturn(Optional.empty());
        when(addProfileService.createNewProfile("Alex", "Smith", "new-user", "alex@example.com", "secret"))
                .thenReturn(created);

        mockMvc.perform(validSignUpRequest("new-user"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/new-user"));

        verify(findProfileService).findByUid("new-user");
        verify(addProfileService).createNewProfile("Alex", "Smith", "new-user", "alex@example.com", "secret");
        assertNotNull(SecurityUtil.getCurrentProfile());
        assertEquals("new-user", SecurityUtil.getCurrentProfile().getUsername());
        assertEquals(Long.valueOf(11L), SecurityUtil.getCurrentProfile().getId());
    }

    @Test
    public void shouldRejectDuplicateUidWithoutCreatingOrAuthenticatingProfile() throws Exception {
        when(findProfileService.findByUid("existing")).thenReturn(Optional.of(profile(2L, "existing", "old")));

        mockMvc.perform(validSignUpRequest("existing"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Incorrect data"));

        verify(findProfileService).findByUid("existing");
        verify(addProfileService, never()).createNewProfile("Alex", "Smith", "existing", "alex@example.com", "secret");
        assertNull(SecurityUtil.getCurrentProfile());
    }

    @Test
    public void shouldReturnBadRequestWhenRequiredSignUpParameterIsMissing() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("first_name", "Alex")
                .param("last_name", "Smith")
                .param("uid", "new-user")
                .param("mail", "alex@example.com"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(findProfileService, addProfileService);
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder validSignUpRequest(String uid) {
        return post("/sign-up")
                .param("first_name", "Alex")
                .param("last_name", "Smith")
                .param("uid", uid)
                .param("mail", "alex@example.com")
                .param("password", "secret");
    }

    private Profile profile(long id, String uid, String password) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setFirstName("Alex");
        profile.setLastName("Smith");
        profile.setUid(uid);
        profile.setPassword(password);
        return profile;
    }
}
