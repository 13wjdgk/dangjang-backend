package com.coniverse.dangjang.domain.auth.service;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.auth.dto.AuthToken.AuthToken;
import com.coniverse.dangjang.domain.auth.dto.Response.LoginResponse;
import com.coniverse.dangjang.domain.auth.dto.request.OauthLoginParam;
import com.coniverse.dangjang.domain.auth.service.authToken.AuthTokensGenerator;
import com.coniverse.dangjang.domain.user.dto.UserInfo;
import com.coniverse.dangjang.domain.user.infrastructure.OAuthInfoResponse;
import com.coniverse.dangjang.domain.user.service.UserService;
import com.coniverse.dangjang.global.exception.NonExistentUserException;

import lombok.RequiredArgsConstructor;

/**
 * @author Eve
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class OauthLoginService {
	private final AuthTokensGenerator authTokensGenerator;
	private final com.coniverse.dangjang.domain.auth.service.OauthInfoService OauthInfoService;
	private final UserService userService;

	/**
	 * @param params 카카오,네이버 accessToken을 받아온다.
	 * @return Content 로그인을 성공하면, JWT TOKEN과 사용자 정보(nickname, authID)를 전달한다.
	 * @throws NonExistentUserException 회원가입된 유저가 아닐때 발생하는 오류
	 * @since 1.0
	 */
	public LoginResponse login(OauthLoginParam params) {

		/**
		 * 카카오,네이버 사용자 정보 조회
		 * @since 1.0
		 */
		OAuthInfoResponse oAuthInfoResponse = OauthInfoService.request(params);
		/**
		 * 유저 존재 여부
		 * @since 1.0
		 */
		UserInfo user = userService.findUser(oAuthInfoResponse);

		/**
		 * AuthToken 반환 (JWT)
		 * @since 1.0
		 */
		AuthToken authToken = authTokensGenerator.generate(user.getOauthId());
		/**
		 * 로그인 요청 response 객체 생성
		 * @since 1.0
		 */
		return new LoginResponse(user.getOauthId(), user.getNickname(),
			authToken.getAccessToken(), authToken.getRefreshToken(), authToken.getExpiresIn());
	}

}