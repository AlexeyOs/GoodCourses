package net.os.goodcourses.model;

import java.util.Collections;

import net.os.goodcourses.entity.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import net.os.goodcourses.Constants;

/**
 *
 */
public class CurrentProfile extends User {
	private static final long serialVersionUID = 3850489832510630519L;
	private static final String NO_LOCAL_PASSWORD = "{external-only}";
	private final Long id;
	private final String fullName;

	public CurrentProfile(Profile profile) {
		super(profile.getUid(), profile.getPassword() == null ? NO_LOCAL_PASSWORD : profile.getPassword(),
				true, true, true, true,
				Collections.singleton(new SimpleGrantedAuthority(Constants.USER)));
		this.id = profile.getId();
		this.fullName = profile.getFullName();
	}

	public Long getId() {
		return id;
	}
	
	public String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return String.format("CurrentProfile [id=%s, username=%s]", id, getUsername());
	}
}
