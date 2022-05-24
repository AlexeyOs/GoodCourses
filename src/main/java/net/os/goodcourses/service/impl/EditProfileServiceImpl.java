package net.os.goodcourses.service.impl;

import net.os.goodcourses.exception.CantCompleteClientRequestException;
import net.os.goodcourses.repository.storage.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.form.SignUpForm;
import net.os.goodcourses.service.EditProfileService;
import net.os.goodcourses.util.DataUtil;

@Service
@SuppressWarnings("unchecked")
public class EditProfileServiceImpl implements EditProfileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EditProfileServiceImpl.class);
	@Autowired
	private ProfileRepository profileRepository;

	@Value("${generate.uid.suffix.length}")
	private int generateUidSuffixLength;

	@Value("${generate.uid.alphabet}")
	private String generateUidAlphabet;

	@Value("${generate.uid.max.try.count}")
	private int maxTryCountToGenerateUid;

	@Override
	@Transactional
	public Profile createNewProfile(SignUpForm signUpForm) {
		Profile profile = new Profile();
		profile.setUid(generateProfileUid(signUpForm));
		profile.setFirstName(DataUtil.capitalizeName(signUpForm.getFirstName()));
		profile.setLastName(DataUtil.capitalizeName(signUpForm.getLastName()));
		profile.setPassword(signUpForm.getPassword());
		profile.setCompleted(false);
		profileRepository.save(profile);
		return profile;
	}

	@Override
	@Transactional
	public void updatePassword(Profile profile, String password) {
		profile.setPassword(password);
		profileRepository.save(profile);
	}

	private String generateProfileUid(SignUpForm signUpForm) throws CantCompleteClientRequestException {
		String baseUid = DataUtil.generateProfileUid(signUpForm);
		String uid = baseUid;
		for (int i = 0; profileRepository.countByUid(uid) > 0; i++) {
			uid = DataUtil.regenerateUidWithRandomSuffix(baseUid, generateUidAlphabet, generateUidSuffixLength);
			if (i >= maxTryCountToGenerateUid) {
				throw new CantCompleteClientRequestException("Can't generate unique uid for profile: " + baseUid + ": maxTryCountToGenerateUid detected");
			}
		}
		return uid;
	}

}
