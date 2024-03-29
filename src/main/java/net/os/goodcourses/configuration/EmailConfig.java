package net.os.goodcourses.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Autowired
	private ConfigurableEnvironment environment;
	
	@Bean
	public JavaMailSender javaMailSender(){
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(environment.getRequiredProperty("email.smtp.server"));
		if(environment.containsProperty("email.smtp.username")){
			javaMailSender.setUsername(environment.resolveRequiredPlaceholders(environment.getRequiredProperty("email.smtp.username")));
			javaMailSender.setPassword(environment.resolveRequiredPlaceholders(environment.getRequiredProperty("email.smtp.password")));
			javaMailSender.setPort(Integer.parseInt(environment.getRequiredProperty("email.smtp.port")));
			javaMailSender.setDefaultEncoding("UTF-8");
			javaMailSender.setJavaMailProperties(javaMailProperties());
		}
		return javaMailSender;
	}
	
	private Properties javaMailProperties(){
		Properties p = new Properties();
		p.setProperty("mail.smtp.auth", "true");
		p.setProperty("mail.smtp.starttls.enable", "true");
		p.setProperty("mail.smtp.socketFactory.fallback", "false");
		p.setProperty("mail.debug", "true");
		p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.setProperty("mail.store.protocol", "pop3");
		p.setProperty("mail.transport.protocol", "smtp");
		p.setProperty("mail.smtp.starttls.required", "true");
		p.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		return p;
	}
}
