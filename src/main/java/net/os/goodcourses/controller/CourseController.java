package net.os.goodcourses.controller;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.CurrentProfile;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.AddCourseService;
import net.os.goodcourses.service.impl.AddFeedBackServiceImpl;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AddCourseService addCourseService;

    @Autowired
    private ProfileRepository profileRepository;

    @RequestMapping(value = "/add/course", method = RequestMethod.GET)
    public String signUp() {
        return "/add/course";
    }

    @RequestMapping(value = "/add/course", method= RequestMethod.POST)
    public String addCourse(@RequestParam("platform") String platform,
                                    @RequestParam("author") String author,
                                    @RequestParam("subjectOfStudy") String subjectOfStudy,
                                    @RequestParam("link") String link,
                                    Model model) {
        //TODO добавить пользователя в сущность курса
//        long uid = SecurityUtil.getCurrentIdProfile();
//        Profile profile = profileRepository.findOne(uid);
        model.addAttribute("alertMsg", "Данные успешно добавлены");
        addCourseService.createNewCourse(platform, author, subjectOfStudy, link);
        return "redirect:/courses";
    }
}
