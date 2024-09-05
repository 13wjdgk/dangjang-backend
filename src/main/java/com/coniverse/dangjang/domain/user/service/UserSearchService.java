package com.coniverse.dangjang.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coniverse.dangjang.domain.healthmetric.enums.HealthConnect;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.entity.UserAccess;
import com.coniverse.dangjang.domain.user.exception.NonExistentUserException;
import com.coniverse.dangjang.domain.user.repository.UserAccessRepository;
import com.coniverse.dangjang.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * user search service
 *
 * @author TEO
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSearchService {
	private final UserRepository userRepository;
	private final UserAccessRepository userAccessRepository;

	/**
	 * PK로 유저를 조회한다.
	 *
	 * @param oauthId 유저 PK
	 * @return User 유저
	 * @throws NonExistentUserException 유저가 존재하지 않을 경우 발생하는 예외
	 * @since 1.0.0
	 */
	public User findUserByOauthId(String oauthId) {
		return userRepository.findById(oauthId).orElseThrow(NonExistentUserException::new);
	}

	/**
	 * 닉네임 조회
	 *
	 * @param nickname 사용자 닉네임
	 * @return User 사용자
	 * @since 1.0.0
	 */
	public Optional<User> findNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	/**
	 * 헬스커넥트 연동 여부를 조회한다.
	 *
	 * @param oauthId 사용자 PK
	 * @return boolean 헬스커넥트 연동 여부
	 */
	public HealthConnect findInterlockHealthConnect(String oauthId) {
		return userRepository.findHealthConnectByOauthId(oauthId);
	}

	/**
	 * 사용자의 최근 접속일을 조회한다.
	 *
	 * @since 1.6.0
	 */
	public UserAccess findUserAccessByOauthId(String oauthId) {
		return userAccessRepository.findFirstByOauthIdOrderByLastAccessDateDesc(oauthId);
	}
}
