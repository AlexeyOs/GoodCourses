package net.os.goodcourses.controller;

import net.os.goodcourses.Constants;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.FindProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PrivateDataController {

    @Autowired
    private FindProfileService findProfileService;

    @RequestMapping(value = { "/profiles" })
    public String listAll(Model model) {
        Page<Profile> profiles = findProfileService.findAll(PageRequest.of(0, Constants.MAX_PROFILES_PER_PAGE, Sort.by(Sort.Direction.ASC,"id")));
        model.addAttribute("profiles", profiles.getContent());
        model.addAttribute("page", profiles);
        return "profiles";
    }
}
