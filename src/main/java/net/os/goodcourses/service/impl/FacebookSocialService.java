package net.os.goodcourses.service.impl;

import net.os.goodcourses.repository.storage.ProfileRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfb.types.User;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.SocialService;

import java.util.Optional;

@Service
public class FacebookSocialService implements SocialService<User> {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Override
	public Optional<Profile> loginViaSocialNetwork(User model) {
		if(StringUtils.isNotBlank(model.getEmail())) {
			Optional<Profile> p = profileRepository.findByEmail(model.getEmail());
			if(p != null){
				return p;
			}
		}
		//TODO Signup required here
		return null;
	}

}
