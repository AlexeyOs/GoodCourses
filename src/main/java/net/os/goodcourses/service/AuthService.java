package net.os.goodcourses.service;

import org.springframework.ui.Model;
public interface AuthService {
    String passwordReset(String mailOrLogin, Model model);

    boolean isPasswordResetTokenValid(long profileId, String token);

    boolean changePassword(long profileId, String token, String password);
}
