package net.os.goodcourses.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import net.os.goodcourses.Constants;
import net.os.goodcourses.util.SecurityUtil;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
		JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
		persistentTokenRepository.setDataSource(dataSource);
		return persistentTokenRepository;
	}

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return SecurityUtil.securityContextRepository();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			DaoAuthenticationProvider authenticationProvider,
			UserDetailsService userDetailsService,
			PersistentTokenRepository persistentTokenRepository,
			SecurityContextRepository securityContextRepository,
			@Value("${REMEMBER_ME_KEY}") String rememberMeKey) throws Exception {
		http
			.authenticationProvider(authenticationProvider)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/health/live", "/health/ready").permitAll()
				.requestMatchers("/my-profile", "/profiles", "/add", "/add/**", "/edit", "/edit/**",
						"/my-reviews", "/my-reviews/**", "/remove")
				.hasAuthority(Constants.USER)
				.anyRequest().permitAll())
			.formLogin(form -> form
				.loginPage("/sign-in")
				.loginProcessingUrl("/sign-in-handler")
				.usernameParameter("uid")
				.passwordParameter("password")
				.defaultSuccessUrl("/my-profile")
				.failureUrl("/sign-in-failed"))
			.logout(logout -> logout
				.logoutUrl("/sign-out")
				.logoutSuccessUrl("/courses")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID"))
			.rememberMe(rememberMe -> rememberMe
				.rememberMeParameter("remember-me")
				.key(rememberMeKey)
				.userDetailsService(userDetailsService)
				.tokenRepository(persistentTokenRepository))
			.securityContext(securityContext -> securityContext
				.securityContextRepository(securityContextRepository)
				.requireExplicitSave(true))
			.csrf(Customizer.withDefaults());

		return http.build();
	}
}
