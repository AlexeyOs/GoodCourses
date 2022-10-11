package net.os.goodcourses.model;

import java.util.Collections;

import lombok.Getter;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.enums.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


@Getter
public class CurrentProfile extends User {
	private static final long serialVersionUID = 3850489832510630519L;
	private final Long id;
	private final String fullName;

	public CurrentProfile(Profile profile) {
		super(profile.getUid(),
				profile.getPassword(),
				true,
				true,
				true,
				true,
				//TODO убрать хардкод, сделать считываение роли из колонки
				profile.getUid().contains("adm") ?
					Collections.singleton(new SimpleGrantedAuthority(RoleType.ADMIN.toString())) :
					Collections.singleton(new SimpleGrantedAuthority(RoleType.USER.toString()))
		);
		this.id = profile.getId();
		this.fullName = profile.getFullName();
	}

	@Override
	public String toString() {
		return String.format("CurrentProfile [id=%s, username=%s]", id, getUsername());
	}
}
