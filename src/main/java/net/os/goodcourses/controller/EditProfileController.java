package net.os.goodcourses.controller;


import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.FindProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.os.goodcourses.model.CurrentProfile;
import net.os.goodcourses.service.EditProfileService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class EditProfileController {

	@Autowired
	private EditProfileService editProfileService;

	@Autowired
	private FindProfileService findProfileService;

	@RequestMapping(value = "/edit/profile", method = RequestMethod.GET)
	public String getEditProfile(@AuthenticationPrincipal CurrentProfile currentProfile, Model model) {
		Optional<Profile> profile = findProfileService.findById(currentProfile.getId());
		profile.ifPresent(value -> model.addAttribute("profile", value));
		return "edit/profile";
	}

	@RequestMapping(value = "/edit/profile", method = RequestMethod.POST)
	public String updateProfileInfo(@RequestParam("summary") String summary,
									@AuthenticationPrincipal CurrentProfile currentProfile) {
		Optional<Profile> profile = findProfileService.findById(currentProfile.getId());
		profile.ifPresent(value -> editProfileService.updateInfo(value, summary));
		return "redirect:/" + currentProfile.getUsername();
	}


	@RequestMapping(value = "/my-profile")
	public String getMyProfile(@AuthenticationPrincipal CurrentProfile currentProfile) {
		if (Optional.ofNullable(currentProfile).isPresent()) {
			return "redirect:/" + currentProfile.getUsername();
		} else {
			return "redirect:/sign-in";
		}
	}
}
