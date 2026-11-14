package net.os.goodcourses.util;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.os.goodcourses.entity.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import net.os.goodcourses.model.CurrentProfile;

/**
 *
 */
public final class SecurityUtil {
	private static final SecurityContextRepository SECURITY_CONTEXT_REPOSITORY =
			new HttpSessionSecurityContextRepository();

	private SecurityUtil() {
	}

	public static SecurityContextRepository securityContextRepository() {
		return SECURITY_CONTEXT_REPOSITORY;
	}

	public static CurrentProfile getCurrentProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof CurrentProfile) {
			return ((CurrentProfile) principal);
		} else {
			return null;
		}
	}

	public static Long getCurrentIdProfile() {
		CurrentProfile currentProfile = getCurrentProfile();
		return currentProfile != null ? currentProfile.getId() : null;
	}

	public static void authentificate(Profile profile, HttpServletRequest request, HttpServletResponse response) {
		CurrentProfile currentProfile = new CurrentProfile(profile);
		currentProfile.eraseCredentials();
		Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
				currentProfile, null, currentProfile.getAuthorities());
		if (request.getSession(false) != null) {
			request.changeSessionId();
		}
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
		SECURITY_CONTEXT_REPOSITORY.saveContext(securityContext, request, response);
	}

	public static boolean isCurrentProfileAuthentificated() {
		return getCurrentProfile() != null;
	}

	public static String generateNewActionUid() {
		return UUID.randomUUID().toString();
	}

	public static String generateNewRestoreAccessToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
