package net.os.goodcourses.model;

import java.util.Collections;

import lombok.Getter;
import net.os.goodcourses.entity.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import net.os.goodcourses.Constants;


@Getter
public class CurrentProfile extends User {
	private static final long serialVersionUID = 3850489832510630519L;
	private final Long id;
	private final String fullName;

	public CurrentProfile(Profile profile) {
		super(profile.getUid(), profile.getPassword(), true, true, true, true, Collections.singleton(new SimpleGrantedAuthority(Constants.USER)));
		this.id = profile.getId();
		this.fullName = profile.getFullName();
	}

	@Override
	public String toString() {
		return String.format("CurrentProfile [id=%s, username=%s]", id, getUsername());
	}
}
