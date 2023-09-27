package com.coniverse.dangjang.domain.auth.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coniverse.dangjang.domain.auth.service.JwtTokenProvider;
import com.coniverse.dangjang.domain.point.service.PointService;
import com.coniverse.dangjang.domain.user.repository.UserRepository;
import com.coniverse.dangjang.global.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 검증 필터
 *
 * @author EVE
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
	private final PointService pointService;
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer";

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws
		ServletException,
		IOException {
		String token = getToken(request.getHeader(AUTHORIZATION));

		if (validationToken(token)) {
			Authentication auth = getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String authorization = request.getHeader(AUTHORIZATION);
		return authorization == null || !authorization.startsWith(BEARER);
	}

	/**
	 * @param token
	 * @return boolean 토큰 유효성 확인
	 * @throws InvalidTokenException // TODO 각 exception이 언제 발생하는지
	 * @since 1.0.0
	 */
	private boolean validationToken(String token) {
		try {
			Key key = jwtTokenProvider.getKey();
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Authentication 을 생성한다
	 *
	 * @param token
	 * @return Authentication 접근한 사용자 정보
	 * @since 1.0.0
	 */
	private Authentication getAuthentication(String token) {
		Claims claims = jwtTokenProvider.parseClaims(token);

		String oauthId = claims.getSubject();

		final Collection<? extends GrantedAuthority> authorities = Stream.of(
				claims.get("role").toString())
			.map(SimpleGrantedAuthority::new)
			.toList();

		User principal = new User(claims.getSubject(), oauthId, authorities);
		checkAccessPoint(oauthId);
		return new UsernamePasswordAuthenticationToken(principal, oauthId, authorities);
	}

	/**
	 * 토큰 값을 분리
	 *
	 * @param authHeader
	 * @return 토큰 값
	 * @since 1.0.0
	 */

	private String getToken(final String authHeader) {
		if (StringUtils.hasLength(authHeader) && authHeader.startsWith(BEARER)) {
			return authHeader.substring(7); //TODO JWT Bearer 스페이스바 수정
		} else {
			throw new InvalidTokenException("잘못된 Authorization Header 입니다.");
		}

	}

	/**
	 * 접속 포인트 적립
	 *
	 * @param oauthId
	 * @since 1.0.0
	 */

	private void checkAccessPoint(String oauthId) {
		pointService.addAccessPoint(oauthId);
	}
}
