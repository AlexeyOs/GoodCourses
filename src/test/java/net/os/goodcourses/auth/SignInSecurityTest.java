package net.os.goodcourses.auth;

import net.os.goodcourses.configuration.SecurityConfig;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.CurrentProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = SignInSecurityTest.TestConfiguration.class)
public class SignInSecurityTest {

    @Autowired private WebApplicationContext context;
    @Autowired private UserDetailsService userDetailsService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        reset(userDetailsService);
        mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldAuthenticateWithValidCredentials() throws Exception {
        when(userDetailsService.loadUserByUsername("alex"))
                .thenReturn(new CurrentProfile(profile(5L, "alex", "secret")));

        mockMvc.perform(formLogin("/sign-in-handler")
                .userParameter("uid")
                .user("alex")
                .password("secret"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/my-profile"))
                .andExpect(authenticated().withUsername("alex"));
    }

    @Test
    public void shouldRejectInvalidPassword() throws Exception {
        when(userDetailsService.loadUserByUsername("alex"))
                .thenReturn(new CurrentProfile(profile(5L, "alex", "secret")));

        mockMvc.perform(formLogin("/sign-in-handler")
                .userParameter("uid")
                .user("alex")
                .password("wrong-password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/sign-in-failed"))
                .andExpect(unauthenticated());
    }

    @Test
    public void shouldRedirectGuestFromProtectedUrlToSignIn() throws Exception {
        mockMvc.perform(get("/my-profile"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/sign-in"))
                .andExpect(unauthenticated());
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

    @Configuration
    @EnableWebMvc
    @Import(SecurityConfig.class)
    static class TestConfiguration {
        @Bean
        UserDetailsService userDetailsService() {
            return org.mockito.Mockito.mock(UserDetailsService.class);
        }

        @Bean
        DataSource dataSource() {
            return org.mockito.Mockito.mock(DataSource.class);
        }

        @Bean
        ProtectedController protectedController() {
            return new ProtectedController();
        }
    }

    @RestController
    static class ProtectedController {
        @GetMapping("/my-profile")
        String profile() {
            return "profile";
        }
    }
}
