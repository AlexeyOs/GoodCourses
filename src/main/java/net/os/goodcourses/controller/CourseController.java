package net.os.goodcourses.controller;

import net.os.goodcourses.Constants;
import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.AddCourseService;
import net.os.goodcourses.service.FindCourseService;
import net.os.goodcourses.service.FindProfileService;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private AddCourseService addCourseService;

    @Autowired
    private FindCourseService findCourseService;

    @Autowired
    private FindProfileService findProfileService;

    @Autowired
    private ProfileRepository profileRepository;

    @RequestMapping(value = "/add/course", method = RequestMethod.GET)
    public String signUp() {
        return "/add/course";
    }

    @RequestMapping(value = "/add/course", method = RequestMethod.POST)
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

    @RequestMapping(value = "course/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = { "/courses" })
    public String courseAll(Model model) {
        Page<Course> courses = findCourseService.findAll(PageRequest.of(0, Constants.MAX_PROFILES_PER_PAGE, Sort.by(Sort.Direction.ASC, "id")));
        model.addAttribute("courses", courses.getContent());
        model.addAttribute("page", courses);
        if (SecurityUtil.getCurrentIdProfile() != null) {
            model.addAttribute("auth", true);
        }
        return "courses";
    }
}
