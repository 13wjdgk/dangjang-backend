package com.coniverse.dangjang.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coniverse.dangjang.domain.user.entity.UserAccess;

/**
 * UserAccess Repository
 *
 * @since 1.6.0
 */
public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

	/**
	 * 사용자의 최근 접속일을 조회한다.
	 *
	 * @since 1.6.0
	 */
	UserAccess findFirstByOauthIdOrderByLastAccessDateDesc(String oauthId);
}
