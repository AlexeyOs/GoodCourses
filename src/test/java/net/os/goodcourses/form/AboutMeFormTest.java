package net.os.goodcourses.form;

import net.os.goodcourses.entity.Profile;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AboutMeFormTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    public void shouldStoreAboutMeInProfile() {
        Profile profile = new Profile();
        profile.setAboutMe("Java developer and lifelong learner");

        assertEquals("Java developer and lifelong learner", profile.getAboutMe());
    }

    @Test
    public void shouldRejectAboutMeLongerThanFiveThousandCharacters() {
        AboutMeForm form = new AboutMeForm(new String(new char[5001]).replace('\0', 'a'));

        assertTrue(validator.validate(form).stream()
                .anyMatch(violation -> "aboutMe".equals(violation.getPropertyPath().toString())));
    }
}
