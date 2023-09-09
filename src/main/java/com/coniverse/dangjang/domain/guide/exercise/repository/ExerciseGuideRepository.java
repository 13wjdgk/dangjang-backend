package com.coniverse.dangjang.domain.guide.exercise.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.coniverse.dangjang.domain.guide.exercise.document.ExerciseGuide;

/**
 * 운동 가이드 Repository
 *
 * @author EVE
 * @since 1.0.0
 */
public interface ExerciseGuideRepository extends MongoRepository<ExerciseGuide, String> {
	/**
	 * 운동 가이드 조회
	 *
	 * @param oauthId   사용자 ID
	 * @param createdAt 생성일
	 * @return 운동 가이드
	 * @since 1.0.0
	 */
	@Query("{'oauthId': ?0, 'createdAt': ?1}")
	Optional<ExerciseGuide> findByOauthIdAndCreatedAt(String oauthId, LocalDate createdAt);
}
