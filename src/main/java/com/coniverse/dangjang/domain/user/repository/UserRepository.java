package com.coniverse.dangjang.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coniverse.dangjang.domain.healthmetric.enums.HealthConnect;
import com.coniverse.dangjang.domain.user.entity.User;

/**
 * USER Repository
 *
 * @author EVE
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, String> {
	/**
	 * nickname으로 사용자 정보 조회
	 *
	 * @since 1.0.0
	 */
	Optional<User> findByNickname(String nickname);

	/**
	 * health connect 연동 여부 조회
	 *
	 * @param oauthId 사용자 pk
	 * @return HealthConnect 연동여부
	 * @since 1.1.0
	 */
	@Query("SELECT u.healthConnect FROM users u WHERE u.oauthId = :oauthId")
	HealthConnect findHealthConnectByOauthId(String oauthId);
}
