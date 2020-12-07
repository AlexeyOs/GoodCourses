package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.AddProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AddProfileServiceImpl implements AddProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddFeedBackServiceImpl.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Profile createNewProfile(String firstName,
                                    String lastName,
                                    String uid,
                                    String mail,
                                    String password) {
            Profile profile = new Profile();
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setUid(uid);
            profile.setEmail(mail);
            profile.setPassword(password);
            profileRepository.save(profile);
            return profile;
    }
}
