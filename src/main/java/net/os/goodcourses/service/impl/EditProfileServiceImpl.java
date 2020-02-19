package net.os.goodcourses.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.os.goodcourses.exception.CantCompleteClientRequestException;
import net.os.goodcourses.repository.search.ProfileSearchRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.repository.storage.SkillCategoryRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.entity.Skill;
import net.os.goodcourses.entity.SkillCategory;
import net.os.goodcourses.form.SignUpForm;
import net.os.goodcourses.service.EditProfileService;
import net.os.goodcourses.util.DataUtil;

@Service
@SuppressWarnings("unchecked")
public class EditProfileServiceImpl implements EditProfileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EditProfileServiceImpl.class);
	@Autowired
	private ProfileRepository profileRepository;

	@Autowired(required = false)
	private ProfileSearchRepository profileSearchRepository;

	@Autowired
	private SkillCategoryRepository skillCategoryRepository;

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
		registerCreateIndexProfileIfTrancationSuccess(profile);
		return profile;
	}

	private String generateProfileUid(SignUpForm signUpForm) throws CantCompleteClientRequestException {
		String baseUid = DataUtil.generateProfileUid(signUpForm);
		String uid = baseUid;
		for (int i = 0; profileRepository.countByUid(uid) > 0; i++) {
			uid = DataUtil.regenerateUidWithRandomSuffix(baseUid, generateUidAlphabet, generateUidSuffixLength);
			if (i >= maxTryCountToGenerateUid) {
				throw new CantCompleteClientRequestException("Can't generate unique uid for profile: " + baseUid+": maxTryCountToGenerateUid detected");
			}
		}
		return uid;
	}

	private void registerCreateIndexProfileIfTrancationSuccess(final Profile profile) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				LOGGER.info("New profile created: {}", profile.getUid());
				profile.setCertificates(Collections.EMPTY_LIST);
				profile.setSkills(Collections.EMPTY_LIST);
//				profileSearchRepository.save(profile);
				LOGGER.info("New profile index created: {}", profile.getUid());
			}
		});
	}

	@Override
	public List<Skill> listSkills(long idProfile) {
		return profileRepository.findById(idProfile).get().getSkills();
	}

	@Override
	public List<SkillCategory> listSkillCategories() {
		return skillCategoryRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
	}

	@Override
	@Transactional
	public void updateSkills(long idProfile, List<Skill> updatedData) {
		Optional<Profile> profile = profileRepository.findById(idProfile);
		if (CollectionUtils.isEqualCollection(updatedData, profile.get().getSkills())) {
			LOGGER.debug("Profile skills: nothing to update");
			return;
		} else {
			profile.get().setSkills(updatedData);
			profileRepository.save(profile.get());
			registerUpdateIndexProfileSkillsIfTransactionSuccess(idProfile, updatedData);
		}
	}

	private void registerUpdateIndexProfileSkillsIfTransactionSuccess(final long idProfile, final List<Skill> updatedData) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				LOGGER.info("Profile skills updated");
				updateIndexProfileSkills(idProfile, updatedData);
			}
		});
	}

	private void updateIndexProfileSkills(long idProfile, List<Skill> updatedData) {
//		Optional<Profile> profile = profileSearchRepository.findById(idProfile);
//		profile.get().setSkills(updatedData);
//		profileSearchRepository.save(profile.get());
		LOGGER.info("Profile skills index updated");
	}
}
