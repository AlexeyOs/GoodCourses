package net.os.goodcourses.service.impl;

import java.util.concurrent.ExecutorService;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.NotificationMessage;
import net.os.goodcourses.service.NotificationSenderService;

@Service
public class AsyncEmailNotificationSenderService implements NotificationSenderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailNotificationSenderService.class);
	@Autowired
	private ExecutorService executorService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${email.fromEmail}")
	private String fromEmail;

	@Value("${email.fromName}")
	private String fromName;
	
	@Value("${email.sendTryCount}")
	private int tryCount;

	@Value("${email.enabled:false}")
	private boolean enabled;
	
	@Override
	public void sendNotification(NotificationMessage message) {
		if (!enabled) {
			LOGGER.info("Email notifications are disabled");
			return;
		}
		executorService.submit(new EmailItem(message, tryCount));
	}

	@Override
	public String getDestinationAddress(Profile profile) {
		return profile.getEmail();
	}
	
	/**
	 *
	 */
	private class EmailItem implements Runnable {
		private final NotificationMessage notificationMessage;
		private int tryCount;

		private EmailItem(NotificationMessage notificationMessage, int tryCount) {
			super();
			this.notificationMessage = notificationMessage;
			this.tryCount = tryCount;
		}

		@Override
		public void run() {
			try {
				LOGGER.debug("Sending email notification");
				MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage(), false);
				message.setSubject(notificationMessage.getSubject());
				message.setTo(new InternetAddress(notificationMessage.getDestinationAddress(), notificationMessage.getDestinationName()));
				message.setFrom(fromEmail, fromName);
				message.setText(notificationMessage.getContent());
				MimeMailMessage msg = new MimeMailMessage(message);
				javaMailSender.send(msg.getMimeMessage());
				LOGGER.debug("Email notification sent successfully");
			} catch (Exception e) {
				LOGGER.warn("Email notification attempt failed: {}", e.getClass().getSimpleName());
				tryCount--;
				if (tryCount > 0) {
					LOGGER.debug("Retrying email notification: remainingAttempts={}", tryCount);
					executorService.submit(this);
				} else {
					LOGGER.error("Email notification was not sent after all attempts");
				}
			}
		}
	}
}
