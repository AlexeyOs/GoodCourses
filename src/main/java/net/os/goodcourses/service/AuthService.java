package net.os.goodcourses.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    String passwordReset(HttpServletRequest request,
                         @RequestParam(value = "mailOrLogin") String mailOrLogin,
                         Model model);
}
