package net.os.goodcourses.controller;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.CurrentProfile;
import net.os.goodcourses.service.AddProfileService;
import net.os.goodcourses.service.FindProfileService;
import net.os.goodcourses.service.NotificationManagerService;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private FindProfileService findProfileService;

    @Autowired
    private AddProfileService addProfileService;

    @Autowired
    private NotificationManagerService notificationManagerService;

    @RequestMapping(value = "/sign-in")
    public String signIn() {
        CurrentProfile currentProfile = SecurityUtil.getCurrentProfile();
        if (currentProfile != null) {
            return "redirect:/" + currentProfile.getUsername();
        } else {
            return "sign-in";
        }
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestParam(value = "first_name") String firstName,
                         @RequestParam(value = "last_name") String lastName,
                         @RequestParam(value = "uid") String uid,
                         @RequestParam(value = "mail") String mail,
                         @RequestParam(value = "password") String password) {
        if (findProfileService.findByUid(uid).equals(Optional.empty())) {
            Profile profile = addProfileService.createNewProfile(firstName, lastName, uid, mail, password);
            SecurityUtil.authentificate(profile);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/" + profile.getUid());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Incorrect data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/sign-in-failed")
    public String signInFailed(HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") == null) {
            return "redirect:/sign-in";
        }
        return "sign-in";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String signUp() {
        CurrentProfile currentProfile = SecurityUtil.getCurrentProfile();
        if (currentProfile != null) {
            return "redirect:/" + currentProfile.getUsername();
        } else {
            return "sign-up";
        }
    }

    @RequestMapping(value = "/sign-up-failed")
    public String signUpFailed(HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") == null) {
            return "redirect:/sign-up";
        }
        return "sign-up";
    }

    @RequestMapping(value = "/password-reset", method = RequestMethod.GET)
    public String passwordReset() {
        CurrentProfile currentProfile = SecurityUtil.getCurrentProfile();
        if (currentProfile != null) {
            return "redirect:/" + currentProfile.getUsername();
        } else {
            return "password-reset";
        }
    }

    @RequestMapping(value = "/password-reset", method = RequestMethod.POST)
    public String passwordReset(@RequestParam(value = "mail") String mail) {
        Optional<Profile> profile =  findProfileService.findByEmail(mail);
        if (profile.isPresent()) {
            notificationManagerService.sendPasswordChanged(profile.get());
            return "password-reset";
        } else {
            return "redirect:/password-reset-failed";
        }
    }

    @RequestMapping(value = "/password-reset-failed")
    public String passwordResetFailed(Model model) {
        model.addAttribute("alertMsg", "Email в системе не зарегестрирован");
        return "password-reset";
    }
}
