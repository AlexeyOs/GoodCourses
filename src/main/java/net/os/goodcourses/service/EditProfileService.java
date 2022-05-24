package net.os.goodcourses.service;


import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.form.SignUpForm;

/**
 *
 */
public interface EditProfileService {

	Profile createNewProfile(SignUpForm signUpForm);

	void updatePassword(Profile profile, String password);

}
