package net.os.goodcourses.controller;

import net.os.goodcourses.exception.DuplicateFeedbackException;
import net.os.goodcourses.form.FeedbackForm;
import net.os.goodcourses.service.FeedbackService;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/add/feedback", method = RequestMethod.POST)
    public String addFeedBack(@RequestParam("courseId") Long courseId,
                              @Valid @ModelAttribute("feedbackForm") FeedbackForm form,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            preserveFormErrors(form, bindingResult, redirectAttributes);
            return "redirect:/course/" + courseId;
        }

        try {
            feedbackService.create(SecurityUtil.getCurrentIdProfile(), courseId, form);
        } catch (DuplicateFeedbackException exception) {
            bindingResult.rejectValue("description", "feedback.duplicate", exception.getMessage());
            preserveFormErrors(form, bindingResult, redirectAttributes);
        }
        return "redirect:/course/" + courseId;
    }

    private void preserveFormErrors(FeedbackForm form,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("feedbackForm", form);
        redirectAttributes.addFlashAttribute(
                BindingResult.MODEL_KEY_PREFIX + "feedbackForm",
                bindingResult);
    }
}
