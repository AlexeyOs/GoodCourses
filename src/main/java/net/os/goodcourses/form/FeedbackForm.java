package net.os.goodcourses.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class FeedbackForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Введите текст отзыва")
    @Size(max = 2000, message = "Отзыв не должен превышать 2000 символов")
    private String description;

    public FeedbackForm() {
    }

    public FeedbackForm(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
