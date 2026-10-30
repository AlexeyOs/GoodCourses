package net.os.goodcourses.controller;

import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.form.FeedbackForm;
import net.os.goodcourses.service.FeedbackService;
import net.os.goodcourses.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class MyReviewsController {
    private static final int PAGE_SIZE = 10;

    private final FeedbackService feedbackService;

    @Autowired
    public MyReviewsController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/my-reviews")
    public String list(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                       Model model) {
        int safePageNumber = Math.max(pageNumber, 0);
        Page<FeedBack> reviews = feedbackService.findMyReviews(
                SecurityUtil.getCurrentIdProfile(),
                PageRequest.of(safePageNumber, PAGE_SIZE));
        model.addAttribute("reviews", reviews);
        model.addAttribute("page", reviews);
        return "my-reviews";
    }

    @GetMapping("/my-reviews/{id}/edit")
    public String edit(@PathVariable("id") long feedbackId, Model model) {
        FeedBack feedback = feedbackService.findOwned(
                SecurityUtil.getCurrentIdProfile(), feedbackId);
        model.addAttribute("feedback", feedback);
        model.addAttribute("feedbackForm", new FeedbackForm(feedback.getDescription(), feedback.getRating()));
        return "edit/feedback";
    }

    @PostMapping("/my-reviews/{id}/edit")
    public String update(@PathVariable("id") long feedbackId,
                         @Valid @ModelAttribute("feedbackForm") FeedbackForm form,
                         BindingResult bindingResult,
                         Model model) {
        long profileId = SecurityUtil.getCurrentIdProfile();
        FeedBack feedback = feedbackService.findOwned(profileId, feedbackId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("feedback", feedback);
            return "edit/feedback";
        }

        feedbackService.update(profileId, feedbackId, form);
        return "redirect:/my-reviews";
    }

    @PostMapping("/my-reviews/{id}/delete")
    public String delete(@PathVariable("id") long feedbackId) {
        feedbackService.delete(SecurityUtil.getCurrentIdProfile(), feedbackId);
        return "redirect:/my-reviews";
    }
}
