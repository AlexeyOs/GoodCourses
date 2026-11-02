package net.os.goodcourses.repository.storage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

import net.os.goodcourses.entity.ProfileRestore;

/**
 *
 */
public interface ProfileRestoreRepository extends CrudRepository<ProfileRestore, Long> {
	
	Optional<ProfileRestore> findByIdAndTokenHash(Long profileId, String tokenHash);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select restore from ProfileRestore restore join fetch restore.profile where restore.id = :profileId")
	Optional<ProfileRestore> findByIdForUpdate(@Param("profileId") Long profileId);
}
