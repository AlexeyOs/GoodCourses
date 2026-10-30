package net.os.goodcourses.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FeedbackForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Введите текст отзыва")
    @Size(max = 2000, message = "Отзыв не должен превышать 2000 символов")
    private String description;

    @NotNull(message = "Выберите оценку")
    @Min(value = 1, message = "Оценка должна быть от 1 до 5")
    @Max(value = 5, message = "Оценка должна быть от 1 до 5")
    private Integer rating;

    public FeedbackForm() {
    }

    public FeedbackForm(String description) {
        this.description = description;
    }

    public FeedbackForm(String description, Integer rating) {
        this.description = description;
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
