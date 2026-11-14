package net.os.goodcourses.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.ExtendedPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.SocialService;
import net.os.goodcourses.util.SecurityUtil;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FacebookController {

	@Value("${social.facebook.idClient}")
	private String idClient;
	
	@Value("${social.facebook.secret}")
	private String secret;
	
	private String redirectUrl;
	
	@Value("${app.host}")
	public void setRedirectUrl(String appHost) {
		this.redirectUrl = appHost + "/fromFb";
	}

	@Autowired
	private SocialService<User> facebookSocialService;
	
	private String getAuthorizeUrl() {
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(ExtendedPermissions.EMAIL);
		FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_6);
		return client.getLoginDialogUrl(idClient, redirectUrl, scopeBuilder);
	}
	
	@RequestMapping(value = {"/fbLogin"}, method = RequestMethod.GET)
	public String gotoFacebook() {
		return "redirect:" + getAuthorizeUrl();
	}
	
	@RequestMapping(value = {"/fromFb"}, method = RequestMethod.GET)
	public String fromFb(@RequestParam(value = "code", required = false) String code,
			HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(code)) {
			return "redirect:/sign-in";
		}
		FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_6);
		AccessToken accessToken = client.obtainUserAccessToken(idClient, secret, redirectUrl, code);
		client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_6);
		User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email,first_name,last_name"));
		Optional<Profile> p = facebookSocialService.loginViaSocialNetwork(user);
		if (p.isPresent()) {
			SecurityUtil.authentificate(p.get(), request, response);
			return "redirect:/" + p.get().getUid();
		} else {
			return "redirect:/sign-in";
		}
	}
}
