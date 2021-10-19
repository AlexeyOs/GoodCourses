package net.os.goodcourses.controller;

import lombok.AllArgsConstructor;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.CurrentProfile;
import net.os.goodcourses.service.*;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class AuthController {

    private final FindProfileService findProfileService;
    private final AddProfileService addProfileService;
    private final EditProfileService editProfileService;
    private final NotificationManagerService notificationManagerService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final AuthService authService;

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
    public String passwordReset(HttpServletRequest request,
                                @RequestParam(value = "mailOrLogin") String mailOrLogin,
                                Model model) {
        return authService.passwordReset(request, mailOrLogin, model);
    }

    @RequestMapping(value = "/password-reset-failed")
    public String passwordResetFailed(Model model) {
        //TODO хардкод, убрать в конфиг
        model.addAttribute("alertMsg", "Email в системе не зарегестрирован");
        return "password-reset";
    }

    @RequestMapping(value = "/password-change", method = RequestMethod.GET)
    public String passwordChange(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "profileId") String profileId,
                                 Model model) {
        PersistentRememberMeToken persistentRememberMeToken = persistentTokenRepository.getTokenForSeries(profileId);
        if (token.equals(persistentRememberMeToken.getTokenValue())) {
            model.addAttribute("token", token);
            model.addAttribute("profileId", profileId);
            return "password-change";
        } else {
            //TODO хардкод, убрать в конфиг
            model.addAttribute("alertMsg","Некорректная ссылка, попробуйте заново отправить сообщение для сброса пароля");
            return "password-reset";
        }
    }

    @RequestMapping(value = "/password-change", method = RequestMethod.POST)
    public String passwordChange(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "profileId") String profileId,
                                 @RequestParam(value = "password") String password,
                                 Model model) {
        PersistentRememberMeToken persistentRememberMeToken = persistentTokenRepository.getTokenForSeries(profileId);
        if (token.equals(persistentRememberMeToken.getTokenValue())) {
            Optional<Profile> profile =  findProfileService.findById(Long.parseLong(profileId));
            if (profile.isPresent()) {
                editProfileService.updatePassword(profile.get(), password);
                notificationManagerService.sendPasswordChanged(profile.get());
            }
            return "redirect:/sign-in";
        } else {
            model.addAttribute("alertMsg","Некорректная ссылка, попробуйте заново отправить сообщение для сброса пароля");
            return "password-reset";
        }
    }

}
