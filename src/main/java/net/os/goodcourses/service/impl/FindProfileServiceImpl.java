package net.os.goodcourses.service.impl;

import net.os.goodcourses.repository.storage.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.model.CurrentProfile;
import net.os.goodcourses.service.FindProfileService;

import java.util.Optional;

@Service
public class FindProfileServiceImpl implements FindProfileService, UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindProfileServiceImpl.class);

	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public Optional<Profile> findById(long id) {
		return profileRepository.findById(id);
	}

	@Override
	public Optional<Profile> findByUid(String uid) {
		Optional<Profile> profile = profileRepository.findByUid(uid);
		return profile;
	}

	@Override
	public Page<Profile> findAll(Pageable pageable) {
		return profileRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Iterable<Profile> findAllForIndexing() {
		Iterable<Profile> all = profileRepository.findAll();
		for (Profile p : all) {
			p.getSkills().size();
			p.getCourses().size();
		}
		return all;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile profile = findProfile(username);
		return new CurrentProfile(profile);
	}

	private Profile findProfile(String anyUnigueId) throws UsernameNotFoundException {
		Optional<Profile> profile = profileRepository.findByUid(anyUnigueId);
		if (!profile.isPresent()) {
			profile = profileRepository.findByEmail(anyUnigueId);
			if (!profile.isPresent()) {
				profile = profileRepository.findByPhone(anyUnigueId);
			}
		}
		if (profile.isPresent()) {
            return profile.get();
        } else {
            LOGGER.error("Profile not found by " + anyUnigueId);
            throw new UsernameNotFoundException("Profile not found by " + anyUnigueId);
        }
	}
}
