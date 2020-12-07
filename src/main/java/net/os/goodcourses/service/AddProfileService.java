package net.os.goodcourses.service;


import net.os.goodcourses.entity.Profile;

public interface AddProfileService {

    Profile createNewProfile(String firstName, String lastName, String uid, String mail, String password);

}
