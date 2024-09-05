package com.coniverse.dangjang.domain.healthmetric.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.healthmetric.entity.HealthMetric;
import com.coniverse.dangjang.domain.healthmetric.entity.HealthMetricId;

/**
 * 건강지표 repository
 *
 * @author TEO , EVE
 * @since 1.0.0
 */
public interface HealthMetricRepository extends JpaRepository<HealthMetric, HealthMetricId> {
	/**
	 * HealthMetricId로 건강지표 조회
	 *
	 * @param oauthId   사용자 ID
	 * @param type      건강지표
	 * @param createdAt 생성일
	 * @return HealthMetric
	 * @since 1.0.0
	 */
	@Query("SELECT h FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.healthMetricId.createdAt = ?2 AND h.healthMetricId.type = ?3")
	Optional<HealthMetric> findByHealthMetricId(String oauthId, LocalDate createdAt, CommonCode type);

	/**
	 * HealthMetricId로 최근 마지막 건강지표 조회
	 *
	 * @param oauthId    사용자 ID
	 * @param commonCode 건강지표
	 * @return HealthMetric
	 * @since 1.0.0
	 */
	@Query("SELECT h FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.healthMetricId.type = ?2 order by h.healthMetricId.createdAt desc limit 1")
	Optional<HealthMetric> findByHealthMetricId(String oauthId, CommonCode commonCode);

	/**
	 * HealthMetricId로 지난주 건강지표조회
	 *
	 * @param oauthId    사용자 ID
	 * @param commonCode 건강지표
	 * @param startDate  시작일
	 * @param endDate    종료일
	 * @return HealthMetric
	 * @since 1.0.0
	 */
	@Query("SELECT h FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.healthMetricId.type = ?2 and h.healthMetricId.createdAt between ?3 and ?4")
	List<HealthMetric> findLastWeekByHealthMetricId(String oauthId, CommonCode commonCode, LocalDate startDate, LocalDate endDate);

	/**
	 * GroupCode와 oauthId로 지난주 건강지표조회
	 *
	 * @param oauthId   사용자 ID
	 * @param groupCode 그룹코드
	 * @param startDate 시작일
	 * @param endDate   종료일
	 * @return HealthMetric
	 * @since 1.0.0
	 */
	@Query("SELECT h FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.groupCode = ?2 and h.healthMetricId.createdAt between ?3 and ?4")
	List<HealthMetric> findLastWeekByGroupCodeAndCreatedAt(String oauthId, GroupCode groupCode, LocalDate startDate, LocalDate endDate);

	/**
	 * 사용자의 health metrics 마지막 생성일 조회
	 *
	 * @param oauthId 사용자 ID
	 * @return CreatedAt
	 * @since 1.1.0
	 */
	@Query("SELECT h.healthMetricId.createdAt FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 ORDER BY h.healthMetricId.createdAt DESC LIMIT 1")
	Optional<LocalDate> findCreatedAtByOauthId(String oauthId);

	/**
	 * 사용자의 그룹코드와 생성날짜로 조회된 건강지표 개수를 카운트한다
	 *
	 * @param oauthId   사용자 ID
	 * @param groupCode group code
	 * @param createdAt 생성일
	 * @return int count 조회 개수
	 * @since 1.3.0
	 */
	@Query("SELECT count(h) FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.groupCode = ?2 AND h.healthMetricId.createdAt = ?3")
	int findHealthMetricCountByGroupCode(String oauthId, GroupCode groupCode, LocalDate createdAt);

	/**
	 * 기간 내 사용자의 모든 건강지표 조회
	 *
	 * @param oauthId   사용자 ID
	 * @param startDate 시작일
	 * @param endDate   종료일
	 * @return HealthMetric
	 * @since 1.0.0
	 */
	@Query("SELECT h FROM HealthMetric h WHERE h.healthMetricId.oauthId = ?1 AND h.healthMetricId.createdAt between ?2 and ?3")
	List<HealthMetric> findAllHealthMetricByDate(String oauthId, LocalDate startDate, LocalDate endDate);

}
