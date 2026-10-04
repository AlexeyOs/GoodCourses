package net.os.goodcourses.form;

import javax.validation.constraints.Size;

public class AboutMeForm {

    @Size(max = 5000, message = "Раздел «О себе» не должен превышать 5000 символов")
    private String aboutMe;

    public AboutMeForm() {
    }

    public AboutMeForm(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
