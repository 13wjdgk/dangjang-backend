package com.coniverse.dangjang.domain.auth.service.authToken;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.auth.dto.AuthToken.AuthToken;

import lombok.RequiredArgsConstructor;

/**
 * JWT 생성 및 추출
 *
 * @author EVE
 * @since 1.0
 */

@Service
@RequiredArgsConstructor
public class AuthTokensGenerator {
	private static final String BEARER_TYPE = "Bearer";
	@Value("${jwt.access.exp}")
	private long accessTokenExpireTime;
	@Value("${jwt.refresh.exp}")
	private long refreshTokenExpireTime;

	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * JWT 생성
	 *
	 * @param memberId 유저 ID
	 * @return AuthTokens JWT TOKEN 반환
	 * @since 1.0
	 */
	public AuthToken generate(String memberId) {
		long now = (new Date()).getTime();
		Date accessTokenExpiredAt = new Date(now + accessTokenExpireTime);
		Date refreshTokenExpiredAt = new Date(now + refreshTokenExpireTime);

		String subject = memberId;
		String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
		String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

		return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, accessTokenExpireTime / 1000L);
	}

	/**
	 * JWT에서 유저 ID 추출
	 *
	 * @param accessToken JWT 토큰
	 * @return Long 유저ID 반환
	 * @since 1.0
	 */
	public Long extractMemberId(String accessToken) {
		return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
	}
}

