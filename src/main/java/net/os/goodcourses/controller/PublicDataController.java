package net.os.goodcourses.controller;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.os.goodcourses.Constants;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.FindProfileService;

@Controller
public class PublicDataController {

	@Autowired
	private FindProfileService findProfileService;

	@RequestMapping(value = "/{uid}", method = RequestMethod.GET)
	public String getProfile(@PathVariable("uid") String uid, Model model) {
		Optional<Profile> profile = findProfileService.findByUid(uid);
		if (profile.isPresent()) {
			model.addAttribute("profile", profile.get());
			return "profile";
		} else {
			//TODO нужно протестировать
			return "profile_not_found";
		}
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String getError() {
		return "error";
	}

	@RequestMapping(value = "/fragment/more", method = RequestMethod.GET)
	public String moreProfiles(Model model,
							   @PageableDefault(size = Constants.MAX_PROFILES_PER_PAGE) @SortDefault(sort = "id") Pageable pageable) throws UnsupportedEncodingException {
		Page<Profile> profiles = findProfileService.findAll(pageable);
		model.addAttribute("profiles", profiles.getContent());
		return "fragment/profile-items";
	}

}
