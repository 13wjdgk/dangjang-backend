package com.coniverse.dangjang.domain.auth.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.coniverse.dangjang.domain.auth.dto.AuthToken;
import com.coniverse.dangjang.domain.auth.dto.OauthProvider;
import com.coniverse.dangjang.domain.auth.dto.request.OauthLoginRequest;
import com.coniverse.dangjang.domain.auth.dto.response.LoginResponse;
import com.coniverse.dangjang.domain.auth.entity.BlackToken;
import com.coniverse.dangjang.domain.auth.entity.RefreshToken;
import com.coniverse.dangjang.domain.auth.mapper.AuthMapper;
import com.coniverse.dangjang.domain.auth.repository.BlackTokenRepository;
import com.coniverse.dangjang.domain.auth.repository.RefreshTokenRepository;
import com.coniverse.dangjang.domain.healthmetric.enums.HealthConnect;
import com.coniverse.dangjang.domain.infrastructure.auth.client.OAuthClient;
import com.coniverse.dangjang.domain.infrastructure.auth.dto.OAuthInfoResponse;
import com.coniverse.dangjang.domain.notification.service.NotificationService;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.entity.UserAccess;
import com.coniverse.dangjang.domain.user.exception.InvalidAuthenticationException;
import com.coniverse.dangjang.domain.user.exception.NonExistentUserException;
import com.coniverse.dangjang.domain.user.repository.UserRepository;
import com.coniverse.dangjang.domain.user.service.UserSearchService;
import com.coniverse.dangjang.global.exception.BlackTokenException;
import com.coniverse.dangjang.global.exception.InvalidTokenException;
import com.coniverse.dangjang.global.support.enums.JWTStatus;

import io.jsonwebtoken.Claims;

/**
 * oauth 로그인 서비스
 *
 * @author EVE, TEO
 * @since 1.0.0
 */
//TODO : Service 분리 (토큰관련)
@Service
@Transactional
public class DefaultOauthLoginService implements OauthLoginService {
	private final AuthTokenGenerator authTokenGenerator;
	private final UserSearchService userSearchService;
	private final Map<OauthProvider, OAuthClient> clients;
	private final UserRepository userRepository; // TODO 의존성 제거
	private final JwtTokenProvider jwtTokenProvider;
	private final BlackTokenRepository blackTokenRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final NotificationService notificationService;
	private final AuthMapper authMapper;

	public DefaultOauthLoginService(AuthTokenGenerator authTokenGenerator, UserSearchService userSearchService, List<OAuthClient> clients,
		UserRepository userRepository, JwtTokenProvider jwtTokenProvider, BlackTokenRepository blackTokenRepository,
		RefreshTokenRepository refreshTokenRepository, NotificationService notificationService, AuthMapper authMapper) {
		this.authTokenGenerator = authTokenGenerator;
		this.userSearchService = userSearchService;
		this.clients = clients.stream().collect(
			Collectors.toUnmodifiableMap(OAuthClient::getOauthProvider, Function.identity())
		);
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.blackTokenRepository = blackTokenRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.notificationService = notificationService;
		this.authMapper = authMapper;
	}

	/**
	 * @param params 카카오,네이버 accessToken
	 * @return Content 로그인을 성공하면, JWT TOKEN과 사용자 정보(nickname, authID)를 전달한다.
	 * @since 1.0.0
	 */
	@Override
	public LoginResponse login(OauthLoginRequest params) {
		OAuthInfoResponse oAuthInfoResponse = request(params);
		User user = userSearchService.findUserByOauthId(oAuthInfoResponse.getOauthId());
		HealthConnect healthConnect = userSearchService.findInterlockHealthConnect(user.getOauthId());
		user.verifyActiveUser();
		return new LoginResponse(user.getNickname(), false, healthConnect.isConnecting());
	}

	/**
	 * refreshToken 인증 후 AuthToken 재발급
	 *
	 * @param header
	 * @return String 재발급된 AccessToken
	 * @throws InvalidTokenException accessToken이 Redis에 없을 때 예외 발생
	 * @since 1.0.0
	 */
	@Override
	public String reissueToken(String header) {
		String token = jwtTokenProvider.getToken(header);
		if (jwtTokenProvider.validationToken(token).equals(JWTStatus.INVALID)) {
			throw new InvalidTokenException(JWTStatus.INVALID.getMessage());
		}
		//TODO : 토큰 호출시 자동 제거 기능 추가 및 예외 수정
		RefreshToken refreshToken = refreshTokenRepository.findById(token).orElseThrow(() -> new InvalidTokenException(JWTStatus.EXPIRED.getMessage()));
		String oauthId = jwtTokenProvider.parseClaims(refreshToken.getRefreshToken()).getSubject();
		User user = userSearchService.findUserByOauthId(oauthId);
		return getAuthToken(user.getNickname());
	}

	/**
	 * jwtToken 검증
	 * <p>
	 * 만약 유효한 Token이면 Claims을 반환한다.
	 *
	 * @param token
	 * @return Claims
	 * @throws InvalidTokenException 유효하지 않은 토큰일 경우
	 * @since 1.0.0
	 */
	public Claims checkJwtTokenValidation(String token) {
		JWTStatus jwtStatus = jwtTokenProvider.validationToken(token);
		if (jwtStatus.equals(JWTStatus.OK)) {
			return jwtTokenProvider.parseClaims(token);
		}
		throw new InvalidTokenException(jwtStatus.getMessage());
	}

	/**
	 * Auth 생성
	 * <p>
	 * AuthToken을 생성한 후, accessToken,refreshToken을 Redis에 저장한다.
	 *
	 * @param nickname 사용자 닉네임
	 * @return AuthToken 로그인을 성공한 사용자의 authToken을 전달
	 * @since 1.0.0
	 */
	@Override
	//TODO : 함수 나누기
	public String getAuthToken(String nickname) {
		Optional<User> user = userRepository.findByNickname(nickname);
		if (user.isPresent()) {
			AuthToken authToken = authTokenGenerator.generate(user.get().getOauthId(), user.get().getRole());
			Claims claim = checkJwtTokenValidation(authToken.getRefreshToken());
			long expirationTime = calculateExpirationTime(claim.getExpiration().getTime());
			refreshTokenRepository.save(
				authMapper.toRefreshToken(authToken.getAccessToken(), authToken.getRefreshToken(), expirationTime)
			);
			return authToken.getAccessToken();
		} else {
			throw new NonExistentUserException();
		}
	}

	/**
	 * @param params 카카오,네이버 accessToken
	 * @return OAuthInfoResponse 카카오, 네이버 사용자 정보
	 * @throws InvalidAuthenticationException 유효하지 않은 인증
	 * @since 1.0.0
	 */
	@Override
	public OAuthInfoResponse request(OauthLoginRequest params) {

		try {
			OAuthClient client = clients.get(params.getOauthProvider());
			String accessToken = params.getAccessToken();
			return client.requestOauthInfo(accessToken);
		} catch (HttpClientErrorException.Unauthorized e) {
			throw new InvalidAuthenticationException();
		}

	}

	/**
	 * 유저 접속일자를 업데이트한다.
	 *
	 * @since 1.0.0
	 */
	public void updateUserAccessedAt(User user) {
		UserAccess userAccess = userSearchService.findUserAccessByOauthId(user.getOauthId());
		// TODO: userAccess가 없을 경우 예외처리
	}

	/**
	 * 로그아웃
	 * <p>
	 * 기존 redis에 있는 refreshToken을 제거하고, accessToken을 blackList에 추가
	 *
	 * @param tokenHeader
	 * @since 1.1.0
	 */

	public void logout(String tokenHeader, String fcmToken) {
		String accessToken = jwtTokenProvider.getToken(tokenHeader);
		refreshTokenRepository.deleteById(accessToken);
		makeBlackJwtToken(accessToken);
		notificationService.deleteFcmToken(fcmToken);
	}

	/**
	 * jwtToken을 Black 처리한다
	 *
	 * @param token blackList에 추가될 jwtToken
	 * @since 1.1.0
	 */

	private void makeBlackJwtToken(String token) {
		Claims claim = checkJwtTokenValidation(token);
		long blackTokenExpirationTime = calculateExpirationTime(claim.getExpiration().getTime());
		BlackToken blackToken = BlackToken.builder() //TODO: mapper 패턴으로 변경
			.token(token)
			.expirationTime(blackTokenExpirationTime)
			.build();
		blackTokenRepository.save(blackToken);
	}

	/**
	 * ExpiredTime 계산
	 *
	 * @param expiration jwtToken 만료 시간
	 * @return long 만료 시간까지의 Second
	 * @since 1.1.0
	 */

	private long calculateExpirationTime(long expiration) {
		long now = new Date().getTime();
		return (expiration - now) / 1000;
	}

	/**
	 * Black된 토큰인지 확인한다
	 *
	 * @param token 확인할 토큰
	 * @throws BlackTokenException Black된 토큰일 경우
	 * @since 1.1.0
	 */
	public void validBlackToken(String token) {
		if (blackTokenRepository.findById(token).isPresent()) {
			throw new BlackTokenException();
		}
	}
}