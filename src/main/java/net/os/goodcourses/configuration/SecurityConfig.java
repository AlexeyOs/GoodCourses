package net.os.goodcourses.configuration;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import net.os.goodcourses.enums.RoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final DataSource dataSource;

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
		persistentTokenRepository.setDataSource(dataSource);
		return persistentTokenRepository;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/my-profile",
					"/add",
					"/add/**",
					"/edit",
					"/edit/**",
					"/remove")
				.hasAnyAuthority(RoleType.USER.toString(), RoleType.ADMIN.toString())
				.antMatchers("/profiles")
				.hasAuthority(RoleType.ADMIN.toString())
			.anyRequest().permitAll(); 
		http.formLogin()
			.loginPage("/sign-in")
			.loginProcessingUrl("/sign-in-handler")
			.usernameParameter("uid")
			.passwordParameter("password")
			.defaultSuccessUrl("/my-profile")
			.failureUrl("/sign-in-failed");
		http.logout()
			.logoutUrl("/sign-out")
			.logoutSuccessUrl("/courses")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		http.rememberMe()
			.rememberMeParameter("remember-me")
			.key("user-gc-online")
			.tokenRepository(persistentTokenRepository());
		http.csrf().disable();
	}
}
