package net.os.goodcourses.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.os.goodcourses.entity.Profile;

import java.util.Optional;

/**
 *
 */
public interface FindProfileService {

	Optional<Profile> findById(long id);

	Optional<Profile> findByUid(String uid);

	Optional<Profile> findByEmail(String mail);

	Page<Profile> findAll(Pageable pageable);

	Iterable<Profile> findAllForIndexing();

//	Page<Profile> findBySearchQuery(String query, Pageable pageable);
}
