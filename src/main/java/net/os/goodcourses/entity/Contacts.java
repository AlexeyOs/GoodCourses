package net.os.goodcourses.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@Access(AccessType.FIELD)
public class Contacts implements Serializable {
	private static final long serialVersionUID = -3685720846934765841L;
	
	@Column(length = 80)
	private String skype;

	@Column(length = 255)
	private String vkontakte;

	@Column(length = 255)
	private String facebook;

	@Column(length = 255)
	private String linkedin;

	@Column(length = 255)
	private String github;
	
	@Column(length = 255)
	private String stackoverflow;

	public Contacts() {
		super();
	}

}
