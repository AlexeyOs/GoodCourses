package net.os.goodcourses.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.service.FindCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.os.goodcourses.Constants;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.FindProfileService;
import net.os.goodcourses.util.SecurityUtil;

@Controller
public class PublicDataController {

	@Autowired
	private FindProfileService findProfileService;

	@Autowired
	private FindCourseService findCourseService;

	@RequestMapping(value="/{uid}", method=RequestMethod.GET)
	public String getProfile(@PathVariable("uid") String uid, Model model){
		Optional<Profile> profile = findProfileService.findByUid(uid);
		if(profile.isPresent()) {
			model.addAttribute("profile", profile.get());
			return "profile";
		} else {
			//TODO нужно протестировать
			return "profile_not_found";
		}
	}

	@RequestMapping(value="course/{id}", method=RequestMethod.GET)
	public String getCourse(@PathVariable("id") String id, Model model) {
		Optional<Course> course = findCourseService.findById(id);
		if (course.isPresent()) {
			List<FeedBack> feedBacks = course.get().getFeedbacks();
			model.addAttribute("feedbacks", feedBacks);
			//TODO настройка отображения формы для добавления отзыва
			//TODO в зависимости от того добавлял пользователь ранне отзыв к данному курсу или нет
			if (SecurityUtil.getCurrentIdProfile() != null) {
				long idProfile = SecurityUtil.getCurrentIdProfile();
				Optional<Profile> profile = findProfileService.findById(idProfile);
				boolean editFeedback = false;
				for (FeedBack feedBack : feedBacks) {
					if (profile.isPresent() && feedBack.getProfile().equals(profile.get())) {
						editFeedback = true;
						model.addAttribute("editFeedback", editFeedback);
						break;
					}
				}
				if (!editFeedback) {
					model.addAttribute("editFeedback", editFeedback);
				}
			}
			model.addAttribute("course", course.get());
			return "course";
		} else {
			return "course_not_found";
		}
	}

	@RequestMapping(value="/error", method=RequestMethod.GET)
	public String getError(){
		return "error";
	}


	@RequestMapping(value = { "/courses" })
	public String courseAll(Model model) {
		Page<Course> courses = findCourseService.findAll(PageRequest.of(0, Constants.MAX_PROFILES_PER_PAGE, Sort.by(Sort.Direction.ASC,"id")));
 		model.addAttribute("courses", courses.getContent());
		model.addAttribute("page", courses);
		if (SecurityUtil.getCurrentIdProfile() != null) {
			model.addAttribute("auth", true);
		}
		return "courses";
	}

	@RequestMapping(value = "/fragment/more", method = RequestMethod.GET)
	public String moreProfiles(Model model,
							   @PageableDefault(size=Constants.MAX_PROFILES_PER_PAGE) @SortDefault(sort="id") Pageable pageable) throws UnsupportedEncodingException {
		Page<Profile> profiles = findProfileService.findAll(pageable);
		model.addAttribute("profiles", profiles.getContent());
		return "fragment/profile-items";
	}

}
