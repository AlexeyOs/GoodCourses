package net.os.goodcourses.model;

import lombok.Data;

@Data
public class NotificationMessage {
	private String destinationAddress;
	private String destinationName;
	private String subject;
	private String content;
	
	public NotificationMessage() {
		super();
	}
	public NotificationMessage(String subject, String content) {
		super();
		this.subject = subject;
		this.content = content;
	}

}
