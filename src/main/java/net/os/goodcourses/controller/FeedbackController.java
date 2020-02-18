package net.os.goodcourses.controller;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.AddFeedBackService;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackController {

    @Autowired
    private AddFeedBackService addFeedBackService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @RequestMapping(value = "/add/feedback", method= RequestMethod.POST)
    public String addFeedBack(@RequestParam("id_course") Long id_course,
                                      @RequestParam("feedback") String feedback) {
        Course course = courseRepository.findById(id_course);
        long uid = SecurityUtil.getCurrentIdProfile();
        Profile profile = profileRepository.findOne(uid);
        addFeedBackService.createNewFeedBack(course, profile, feedback);
        return "redirect:/course/" + id_course;
    }
}
