package com.coniverse.dangjang.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.user.dto.UserResponse;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.exception.NonExistentUserException;
import com.coniverse.dangjang.domain.user.infrastructure.OAuthInfoResponse;
import com.coniverse.dangjang.domain.user.repository.UserRepository;

/**
 * USER Service
 *
 * @author EVE
 * @since 1.0
 */
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * 기존 유저 확인
	 *
	 * @param oAuthInfoResponse 카카오,네이버에서 사용자 정보 조회한 데이터 (authID ,Provider)
	 * @return userInfo 기존 유저일 때, 유저 정보를 리턴
	 * @throws NonExistentUserException 회원가입된 유저가 아닐때 발생하는 오류
	 * @since 1.0
	 */

	public UserResponse findUser(OAuthInfoResponse oAuthInfoResponse) throws NonExistentUserException {
		Optional<User> user = userRepository.findByUserId(oAuthInfoResponse.getUserId(), oAuthInfoResponse.getOAuthProvider());
		return new UserResponse(user.orElseThrow(NonExistentUserException::new).getOauthId(), user.get().getNickname());
	}

	/**
	 * 새로운 유저 회원가입
	 *
	 * @param oauthInfoResponse 카카오,네이버에서 사용자 정보 조회한 데이터 (authID ,Provider)
	 * @return 새로 가입된 유저 회원가입
	 * @since 1.0
	 */

	public String signUp(OAuthInfoResponse oauthInfoResponse) {
		User user = User.builder()
			.oauthId(oauthInfoResponse.getUserId())
			.nickname("nickname")
			.oauthProvider(oauthInfoResponse.getOAuthProvider())
			.build();

		return userRepository.save(user).getOauthId();
	}
}
