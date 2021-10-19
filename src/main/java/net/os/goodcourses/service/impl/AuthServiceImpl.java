package net.os.goodcourses.service.impl;

import lombok.AllArgsConstructor;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.*;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FindProfileService findProfileService;
    private final NotificationManagerService notificationManagerService;
    private final PersistentTokenRepository persistentTokenRepository;

    @Override
    public String passwordReset(HttpServletRequest request, String mailOrLogin, Model model) {
        Optional<Profile> profile = mailOrLogin.contains("@") ?
                findProfileService.findByEmail(mailOrLogin) :
                findProfileService.findByUid(mailOrLogin);
        if (profile.isPresent()) {
            PersistentRememberMeToken persistentRememberMeToken = persistentTokenRepository.getTokenForSeries(
                    String.valueOf(profile.get().getId())
            );
            if (persistentRememberMeToken == null) {
                persistentRememberMeToken = new PersistentRememberMeToken(
                        profile.get().getFullName(),
                        String.valueOf(profile.get().getId()),
                        UUID.randomUUID().toString(),
                        new Date()
                );
                persistentTokenRepository.createNewToken(persistentRememberMeToken);

                String url = request.getRequestURL().toString();
                String urlForChangePassword = url.substring(0, url.lastIndexOf("/")) +
                        "/password-change" +
                        "?token=" + persistentRememberMeToken.getTokenValue() +
                        "&profileId=" + profile.get().getId();
                notificationManagerService.sendRestoreAccessLink(profile.get(), urlForChangePassword);
            } else {
                String tokenValue = UUID.randomUUID().toString();
                persistentTokenRepository.updateToken(persistentRememberMeToken.getSeries(),
                        tokenValue,
                        new Date()
                );
                String url = request.getRequestURL().toString();
                String urlForChangePassword = url.substring(0, url.lastIndexOf("/")) +
                        "/password-change" +
                        "?token=" + tokenValue +
                        "&profileId=" + profile.get().getId();
                notificationManagerService.sendRestoreAccessLink(profile.get(), urlForChangePassword);
            }
            //TODO убрать хардкод в конфиг
            model.addAttribute("alertMsg","Вам будет отправлено сообщение, проверьте почту");
            return "password-reset";
        } else {
            return "redirect:/password-reset-failed";
        }
    }
}
