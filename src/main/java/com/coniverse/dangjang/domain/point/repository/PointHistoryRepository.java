package com.coniverse.dangjang.domain.point.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coniverse.dangjang.domain.point.dto.response.UserPointResponse;
import com.coniverse.dangjang.domain.point.entity.PointHistory;
import com.coniverse.dangjang.domain.point.entity.PointId;

/**
 * 포인트 이용 내역 Repository
 *
 * @author EVE
 * @since 1.0.0
 */
public interface PointHistoryRepository extends JpaRepository<PointHistory, PointId> {

	/**
	 * 모든 사용자 포인트 조회
	 * oauthId로 사용자 정보 조회
	 *
	 * @since 1.6.0
	 */

	@Query(name = "findAllUserPoint", nativeQuery = true)
	List<UserPointResponse> findAllUserPoint(@Param("limit") int limit, @Param("offset") int offset);

	/**
	 * 사용자 포인트 조회
	 * oauthId로 사용자 정보 조회
	 *
	 * @since 1.6.0
	 */

	@Query("SELECT ph.pointId.oauthId as oauthId , sum(ph.changePoint) as totalPoint FROM PointHistory ph WHERE ph.pointId.oauthId = ?1 group by ph.pointId.oauthId")
	Optional<UserPointResponse> findUserPoint(String oauthId);

}
