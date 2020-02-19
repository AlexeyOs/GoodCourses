package net.os.goodcourses.repository.storage;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.os.goodcourses.entity.Profile;

/**
 *
 */
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long> {

	Optional<Profile> findById(long id);

	Optional<Profile> findByUid(String uid);

	Optional<Profile> findByEmail(String email);

	Optional<Profile> findByPhone(String phone);
	
	int countByUid(String uid);
	
	Page<Profile> findAllByCompletedTrue(Pageable pageable);
	
	List<Profile> findByCompletedFalseAndCreatedBefore(Timestamp oldDate);
}
